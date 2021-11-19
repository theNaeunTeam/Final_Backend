package com.douzone.final_backend.Service;

import com.douzone.final_backend.Bean.GoodsBean;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.Bean.UserBean;
import com.douzone.final_backend.DAO.OwnerDAO;
import com.douzone.final_backend.DAO.UserDAO;
import com.douzone.final_backend.DTO.FavoritesDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private OwnerDAO ownerDAO;

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

    // 로그인 시 id로 정보 들고와 입력된 비밀번호화 암호화된 비밀번호 match
    public UserBean getByCredentials(final String u_id, final String u_pw, final PasswordEncoder encoder) {
        final UserBean originalUser = userDAO.findByUId(u_id);
        log.info("PW : " + encoder.matches(u_pw, originalUser.getU_pw()));
        log.info("PW?? " + originalUser.getU_pw());

        if (originalUser != null && encoder.matches(u_pw, originalUser.getU_pw())) {
            log.info("originalUser : " + originalUser);
            return originalUser;
        }
        return null;
    }

    // 가게 눌렀을 때 해당 가게의 판매중인 상품 보기
    public List<GoodsBean> storeGoodsView(String o_sNumber) {
        return userDAO.storeGoodsView(o_sNumber);
    }

    // 해당 가게 정보 상세보기
    public OwnerBean findByStore(String o_sNumber) {
        return userDAO.findByStore(o_sNumber);
    }

    // 즐겨찾기 유무 체크 하기
    public boolean favorCheck(FavoritesDTO favoritesDTO){
        return userDAO.favorCheck(favoritesDTO);
    }
}
