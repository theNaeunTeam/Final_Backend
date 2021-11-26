package com.douzone.final_backend.Controller;

import com.douzone.final_backend.Bean.ReserveBean;
import com.douzone.final_backend.Bean.UserBean;
import com.douzone.final_backend.Common.ResponseDTO;
import com.douzone.final_backend.DTO.FavoritesDTO;
import com.douzone.final_backend.DTO.ReserveDTO;
import com.douzone.final_backend.DTO.UserDTO;
import com.douzone.final_backend.Service.UserService;
import com.douzone.final_backend.security.TokenProvider;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    //    @GetMapping("/user")
//    public String test() {
//        List<UserBean> test = userDAO.select();
//        for(UserBean data : test) {
//            System.out.println("data:"+data.getU_email());
//        }
//        log.warn("dmdkdpofiopfi");
//        return "테스트";
//    }
    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/tokencheck")
    public String UserTokenCheck() {
        return "success";
    }


    // 즐겨찾기 유무 확인
    @PostMapping("/favorCheck")
    public ResponseEntity<?> favorView(@RequestBody FavoritesDTO favoritesDTO) {
        log.info("들어온 사업자번호,유저아이디:" + favoritesDTO);
        try {
            boolean result = userService.favorCheck(favoritesDTO);
            log.info("4444444" + result);

            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 즐겨찾기 추가
    @PostMapping("/addFavor")
    public ResponseEntity<?> addFavor(@RequestBody FavoritesDTO favoritesDTO) {
        log.info("즐찾추-들어온 사업자번호,유저아이디:" + favoritesDTO);
        try {
            int result = userService.addFavorService(favoritesDTO);
            log.info("즐찾추api결과:" + result);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 즐겨찾기 해제
    @PostMapping("/FavorOff")
    public ResponseEntity<?> FavorOff(@AuthenticationPrincipal UserDetails userDetails, @RequestBody FavoritesDTO favoritesDTO) {
        log.info("즐찾추-들어온 사업자번호,유저아이디:" + favoritesDTO);
        favoritesDTO.setF_p_user_id(userDetails.getUsername());
        try {
            int result = userService.FavorOffService(favoritesDTO);
            log.info("즐찾해제api결과:" + result);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // user Mypage 로딩
    @GetMapping("myPage")
    public ResponseEntity<?> myPage(@AuthenticationPrincipal UserDetails userDetails) {
        String u_id = userDetails.getUsername();
        try {
            // user 정보가 없을 시 예외 발생
            UserBean user = userService.userData(u_id);
            log.info("user 정보 " + user);
            int save = userService.userSave(u_id);
            int reserve = userService.userReserve(u_id);

            UserDTO responseDTO = UserDTO.builder()
                    .u_id(user.getU_id())
                    .save(save)
                    .u_point(user.getU_point())
                    .reserve(reserve)
                    .build();
            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }


    // 해당 유저 예약 리스트
    @GetMapping("reserveList")
    public ResponseEntity<?> reserveList(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("user reserveList" + userDetails);
        String u_id = userDetails.getUsername();
        try {
            List<ReserveDTO> reserve = userService.reserveList(u_id);
            log.info("user Reserve List : " + reserve);
            return ResponseEntity.ok().body(reserve);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(false);
        }
    }

    // 유저 예약 취소
    @PatchMapping("changeReserveStatus")
    public ResponseEntity<?> changeReserveStatus(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ReserveDTO reserveDTO) {
        log.info("들어온 r_code : " + reserveDTO.getR_code());
        String u_id = userDetails.getUsername();
        reserveDTO.setR_u_id(u_id);

        try {
            ReserveBean reserve = userService.getReserve(reserveDTO);

            ReserveDTO responseDTO = ReserveDTO.builder()
                    .r_count(reserve.getR_count())
                    .r_code(reserve.getR_code())
                    .r_g_code(reserve.getR_g_code())
                    .r_u_id(reserve.getR_u_id())
                    .r_status(reserve.getR_status())
                    .build();
            // r_code 로 검색하고 r_status  1 일 때만 취소됨
            log.info("빌드한 responseDTO : " + responseDTO);

            userService.changeReserveStatus(responseDTO);

            return ResponseEntity.ok().body(true);
        } catch (Exception e) {
            log.error("예약 취소하기에서 에러" + e.getMessage());

            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }

    }

    // user 예약 현황에서 검색
    @GetMapping("searchReserve")
    public ResponseEntity<?> searchReserve(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(required = false) String g_category, @RequestParam(required = false) String r_status, @RequestParam(required = false) String searchInput) {
        log.info("유저 예약 현황에서 검색 : " + g_category + r_status + searchInput);

        String u_id = userDetails.getUsername();
        ReserveDTO r;
        if (r_status.equals("")) {
            r = ReserveDTO.builder()
                    .g_category((g_category))
                    .searchInput(searchInput)
                    .r_status(9999)
                    .r_u_id(u_id)
                    .build();
        } else {
            r = ReserveDTO.builder()
                    .g_category(g_category)
                    .searchInput(searchInput)
                    .r_status(Integer.parseInt(r_status))
                    .r_u_id(u_id)
                    .build();
        }
        log.info("빌드한 r : " + r);

        List<ReserveDTO> responseDTO = userService.searchReserve(r);

        return ResponseEntity.ok().body(responseDTO);
    }

    // user 즐겨찾는 가게 목록
    @GetMapping("favorList")
    public ResponseEntity<?> favorList(@AuthenticationPrincipal UserDetails userDetails) {
        String u_id = userDetails.getUsername();
        // 필요한 정보 -> o_name, o_address, o_time1, o_time2 , o_phone, o_approval
        List<FavoritesDTO> dto = userService.favorList(u_id);

        log.info("favorList : " + dto);

        return ResponseEntity.ok().body(dto);
    }

    // user 회원정보 불러오기
    @GetMapping("userData")
    public ResponseEntity<?> userData(@AuthenticationPrincipal UserDetails userDetails) {
        String u_id = userDetails.getUsername();
        // 필요한 정보 -> u_id, u_pw, u_cellPhone, u_email, u_gender, u_age
        try {

            UserBean user = userService.userData(u_id);
            log.info("user 정보" + user);

            UserDTO responseDTO = UserDTO.builder()
                    .u_id(user.getU_id())
                    .u_cellPhone(user.getU_cellPhone())
                    .u_email(user.getU_email())
                    .u_gender(user.getU_gender())
                    .u_age(user.getU_age())
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 회원 정보 수정
    @PostMapping("userUpdate")
    public ResponseEntity<?> userUpdate(@RequestBody UserDTO userDTO) {
        log.info("회원정보 수정 들어옴  :" + userDTO);
        try {
            String encodePW = passwordEncoder.encode(userDTO.getU_pw());
            UserBean user = UserBean.builder()
                    .u_id(userDTO.getU_id())
                    .u_pw(encodePW)
                    .u_cellPhone(userDTO.getU_cellPhone())
                    .u_email(userDTO.getU_email())
                    .u_gender(userDTO.getU_gender())
                    .u_age(userDTO.getU_age())
                    .build();
            int res = userService.update(user);
            log.info("업데이트 결과:" + res);
            return ResponseEntity.ok().body(res);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping("orderConfirm")
    public ResponseEntity<?> orderConfirm(@AuthenticationPrincipal UserDetails userDetails, @RequestBody List<ReserveDTO> reserveDTO) {
        // r_u_id, r_count, r_g_code, r_customOrder, r_pay, r_owner, r_firstTime,
        log.info("들어오는 값 : " + reserveDTO);
        String u_id = userDetails.getUsername();

        int noShowCount = userService.noShowCount(u_id);
        log.info("노쇼 카운트 : " + noShowCount);
        if (noShowCount < 5) {
            try {// 테이블에 insert, goods 테이블에 상품수 빼고 상품수 0 이면 판매완료로 상태값 바꾸기
                for (ReserveDTO reserve : reserveDTO) {
                    log.info("for문 안");
                    userService.insertReserve(reserve);
                }

                List<String> registrationTokens = userService.getOwnerPushToken(reserveDTO.get(0).getR_g_code());
                MulticastMessage message = MulticastMessage.builder()
                        .putData("title", "새 예약건이 있습니다")
                        .putData("body", reserveDTO.get(0).getG_name() + " 외 " + reserveDTO.get(0).getR_count() + "개")
                        .putData("image", "이미지주소")
                        .putData("r_customOrder",reserveDTO.get(0).getR_customOrder())
                        .addAllTokens(registrationTokens)
                        .build();
                BatchResponse response = null;
                try {
                    response = FirebaseMessaging.getInstance().sendMulticast(message);
                } catch (FirebaseMessagingException e) {
                    e.printStackTrace();
                }

                System.out.println(response.getSuccessCount() + "건 메시지 전송 성공");

                return ResponseEntity.ok().body(true);
            } catch (Exception e) {
                e.printStackTrace();
                ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
                return ResponseEntity
                        .badRequest()
                        .body(responseDTO);
            }
        } else {
            return ResponseEntity
                    .ok()
                    .body(false);
        }
    }

    //회원탈퇴
    @PostMapping("userDelete")
    public ResponseEntity<?> userDelete(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UserDTO userDTO, String u_pw) {
        String u_id = userDetails.getUsername();
        UserBean user = userService.userDelete(
                u_id,
                userDTO.getU_pw(),
                passwordEncoder
        );
        log.info("회원탈퇴user : " + userDTO.getU_pw() + user);

        if (user != null) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }

    }


}

