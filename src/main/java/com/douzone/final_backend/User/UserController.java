package com.douzone.final_backend.User;

import com.douzone.final_backend.security.ResponseDTO;
import com.douzone.final_backend.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
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

    // 회원가입
    @PostMapping("/userjoin")
    public ResponseEntity<?> signup(@RequestBody UserDTO userDTO) {
        try {
            UserBean user = UserBean.builder()
                    .u_id(userDTO.getU_id())
                    .u_pw(userDTO.getU_pw())
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
    public ResponseEntity<?> signin(@RequestBody UserDTO userDTO) {
        UserBean user = userService.getByCredentials(userDTO);
        log.warn(userDTO.getU_id());
        if (user != null) {
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .u_id(user.getU_id())
                    .u_pw(user.getU_pw())
                    .u_cellPhone(user.getU_cellPhone())
                    .u_email(user.getU_email())
                    .u_gender(user.getU_gender())
                    .u_age(user.getU_age())
                    .token(token)
                    .build();
            log.info("로그인 성공");
            return ResponseEntity.ok().body(responseUserDTO);
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login Failed")
                    .build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }


    }


}

