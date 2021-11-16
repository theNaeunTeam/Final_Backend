package com.douzone.final_backend.DAO;

import com.douzone.final_backend.Bean.UserBean;
import com.douzone.final_backend.Bean.GoodsBean;
import com.douzone.final_backend.DTO.UserDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO {
    @Autowired
    private SqlSession sqlSession;

    public List<UserBean> select() {
        return sqlSession.selectList("selectUserAll");
    }


    public boolean existsById(String u_id) {

        return sqlSession.selectOne("existsById", u_id);
    }

    public UserBean findByIdAndPassword(UserDTO userDTO) {
        return sqlSession.selectOne("findByIdAndPassword", userDTO);
    }

    public void insertUser(UserBean userBean) {
        sqlSession.insert("insertUser", userBean);
    }

    public UserBean findByUId(String u_id) {
        return sqlSession.selectOne("findByUId", u_id);
    }

    public UserDTO findByUIdDTO(String id) {
        return sqlSession.selectOne("findByUIdDTO", id);
    }

    public List<GoodsBean> shopView(String o_sNumber) {
        return sqlSession.selectList("shopView", o_sNumber);
    }
}
