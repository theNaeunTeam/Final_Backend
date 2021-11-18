package com.douzone.final_backend.Controller;

import com.douzone.final_backend.Bean.GoodsBean;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.Bean.UserBean;
import com.douzone.final_backend.Common.ResponseDTO;
import com.douzone.final_backend.DTO.OwnerDTO;
import com.douzone.final_backend.DTO.UserDTO;
import com.douzone.final_backend.Service.UserService;
import com.douzone.final_backend.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> storeView(@RequestParam String o_sNumber){
        log.info("storeView 들어오는 값 : "+o_sNumber);
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

}

