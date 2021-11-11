package com.douzone.final_backend.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserDAO userDAO;


    @GetMapping("/user")
    public String test() {
        List<UserBean> test = userDAO.select();
        for(UserBean data : test) {
            System.out.println("data:"+data.getU_email());
        }
        return "테스트";
    }
}
