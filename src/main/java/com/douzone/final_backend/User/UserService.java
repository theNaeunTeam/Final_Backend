package com.douzone.final_backend.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public UserBean create(final UserBean userBean) {
        if (userBean == null || userBean.getU_id() == null) {
            log.warn("데이터 누락");
            throw new RuntimeException("데이터 누락");
        }
        final String u_id = userBean.getU_id();

        if (userDAO.existsById(u_id)) {
            log.warn("이미 존재하는 아이디");
            throw new RuntimeException("존재하는 아이디");
        }

        userDAO.insertUser(userBean);

        return userBean;
    }


    public UserBean getByCredentials(final String u_id, final String u_pw, final PasswordEncoder encoder){
        final UserBean originalUser = userDAO.findByUId(u_id);
        log.info("PW : " +encoder.matches(u_pw,originalUser.getU_pw()));
        log.info("PW?? "+originalUser.getU_pw());

        if(originalUser != null && encoder.matches(u_pw,originalUser.getU_pw())){
            log.info("originalUser : "+originalUser);
            return originalUser;
        }
        return null;
    }
}
