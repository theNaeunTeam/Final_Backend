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

        @PostMapping("/signup")
        public ResponseEntity<?> signup(@RequestBody UserDTO userDTO){
            try {
                UserBean user = UserBean.builder()
                        .u_id(userDTO.getU_id())
                        .u_pw(userDTO.getU_pw())
                        .build();
                UserBean registerUser = userService.create(user);
                UserDTO responseUserDTO = UserDTO.builder()
                        .u_id(registerUser.getU_id())
                        .u_pw(registerUser.getU_pw())
                        .build();

                return ResponseEntity.ok().body(responseUserDTO);
            }catch (Exception e){
                ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
                return ResponseEntity
                        .badRequest()
                        .body(responseDTO);
            }

        }
        @PostMapping("/signin")
        public ResponseEntity<?> signin(@RequestBody UserDTO userDTO){
            UserBean user = userService.getByCredentials(userDTO);

            if(user != null){
                final String token = tokenProvider.create(user);
                final UserDTO responseUserDTO = UserDTO.builder()
                        .u_id(user.getU_id())
                        .u_pw(user.getU_pw())
                        .token(token)
                        .build();
                return ResponseEntity.ok().body(responseUserDTO);
            }else{
                ResponseDTO responseDTO = ResponseDTO.builder()
                        .error("Login Failed")
                        .build();
                return ResponseEntity
                        .badRequest()
                        .body(responseDTO);
            }


        }




    }

