package com.douzone.final_backend.User;

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
}
