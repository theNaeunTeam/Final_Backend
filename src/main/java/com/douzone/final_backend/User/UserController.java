package com.douzone.final_backend.User;

import com.douzone.final_backend.Common.ResponseDTO;
import com.douzone.final_backend.Owner.OwnerBean;
import com.douzone.final_backend.Owner.OwnerDTO;
import com.douzone.final_backend.Owner.OwnerService;
import com.douzone.final_backend.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 회원가입
    @PostMapping("/userjoin")
    public ResponseEntity<?> userjoin(@RequestBody UserDTO userDTO) {
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
            final String id = user.getU_id();
            final String token = tokenProvider.create(id);
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

    @Autowired
    private OwnerService ownerService;

    // 가게 로그인
    @PostMapping("/ownerlogin")
    public ResponseEntity<?> ownerlogin(@RequestBody OwnerDTO ownerDTO) {
        OwnerBean owner = ownerService.getByCredentials(
                ownerDTO.getO_sNumber(),
                ownerDTO.getO_pw(),
                passwordEncoder
        );
        log.info(ownerDTO.getO_pw());

        if (owner != null) {
            final String id = owner.getO_sNumber();
            final String token = tokenProvider.create(id);
            final OwnerDTO responseOwnerDTO = OwnerDTO.builder()
                    .o_sNumber(owner.getO_sNumber())
                    .o_approval(owner.getO_approval())
                    .o_pw(owner.getO_pw())
                    .token(token)
                    .build();
            log.info("로그인 성공");
            return ResponseEntity.ok().body(responseOwnerDTO);
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

