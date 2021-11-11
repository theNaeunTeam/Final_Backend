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
}
