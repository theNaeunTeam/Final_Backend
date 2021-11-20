package com.douzone.final_backend.Controller;

import com.douzone.final_backend.Bean.GoodsBean;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.Bean.ReserveBean;
import com.douzone.final_backend.Bean.UserBean;
import com.douzone.final_backend.Common.ResponseDTO;
import com.douzone.final_backend.DTO.FavoritesDTO;
import com.douzone.final_backend.DTO.OwnerDTO;
import com.douzone.final_backend.DTO.ReserveDTO;
import com.douzone.final_backend.DTO.UserDTO;
import com.douzone.final_backend.Service.UserService;
import com.douzone.final_backend.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
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

    // 회원가입
    @PostMapping("/userjoin")
    public ResponseEntity<?> userjoin(@RequestBody UserDTO userDTO) {
        log.info("회원가입 들어옴" + userDTO);
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
            UserBean registerUser = userService.create(user);
            UserDTO responseUserDTO = UserDTO.builder()
                    .u_id(registerUser.getU_id())
                    .u_pw(registerUser.getU_pw())
                    .u_cellPhone(registerUser.getU_cellPhone())
                    .u_email(registerUser.getU_email())
                    .u_gender(registerUser.getU_gender())
                    .u_age(registerUser.getU_age())
                    .build();
            log.info("회원가입 성공");
            return ResponseEntity.ok().body(responseUserDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();

            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }

    }

    // 로그인
    @PostMapping("/userlogin")
    public ResponseEntity<?> userlogin(@RequestBody UserDTO userDTO) {
        UserBean user = userService.getByCredentials(
                userDTO.getU_id(),
                userDTO.getU_pw(),
                passwordEncoder
        );
        log.info("user : " + userDTO.getU_pw() + user);

        if (user != null) {
            final String id = user.getU_id() + "&USER";
            log.info(id);
            final String token = tokenProvider.create(id);

            final UserDTO responseUserDTO = UserDTO.builder()
//                    .u_id(user.getU_id())
//                    .u_pw(user.getU_pw())
//                    .u_cellPhone(user.getU_cellPhone())
//                    .u_email(user.getU_email())
//                    .u_gender(user.getU_gender())
//                    .u_age(user.getU_age())
//                    .roles(list)
                    .token(token)
                    .build();
            log.info(token);
            log.info("로그인 성공");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            headers.add("X-AUTH-TOKEN", token);
            return new ResponseEntity<>(responseUserDTO, headers, HttpStatus.OK);
//            return ResponseEntity.ok().body(responseUserDTO);
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login Failed")
                    .build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }


    }

    // 예약하기 위한 가게 상세 페이지
    @GetMapping("/storeGoodsView")
    public ResponseEntity<?> shopView(@RequestParam String o_sNumber) {
        log.info(o_sNumber);
        try {
            List<GoodsBean> goodsList = userService.storeGoodsView(o_sNumber);
            log.info("shopView" + goodsList);
            return ResponseEntity.ok().body(goodsList);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 해당 가게 정보 상세보기
    @GetMapping("/storeView")
    public ResponseEntity<?> storeView(@RequestParam String o_sNumber) {
        log.info("storeView 들어오는 값 : " + o_sNumber);
        try {
            OwnerBean ownerBean = userService.findByStore(o_sNumber);
            OwnerDTO responseDTO = OwnerDTO.builder()
                    .o_sNumber(ownerBean.getO_sNumber())
                    .o_address(ownerBean.getO_address())
                    .o_image(ownerBean.getO_image())
                    .o_cellPhone(ownerBean.getO_cellPhone())
                    .o_name(ownerBean.getO_name())
                    .o_phone(ownerBean.getO_phone())
                    .o_time1(ownerBean.getO_time1())
                    .o_time2(ownerBean.getO_time2())
                    .o_latitude(ownerBean.getO_latitude())
                    .o_longitude(ownerBean.getO_longitude())
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
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
                return ResponseEntity.ok().body(true);
            } catch (Exception e) {
                ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
                return ResponseEntity
                        .badRequest()
                        .body(responseDTO);
            }
        }else {
            return ResponseEntity
                    .ok()
                    .body(false);
        }

    }
}

