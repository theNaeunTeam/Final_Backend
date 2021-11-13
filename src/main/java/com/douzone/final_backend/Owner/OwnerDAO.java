package com.douzone.final_backend.Owner;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OwnerDAO {
    @Autowired
    private SqlSession sqlSession;

    public OwnerBean findBySNum(String o_sNumber) {
        return sqlSession.selectOne("findBySNum", o_sNumber);
    }

    public boolean existsBySNum(String o_sNumber) {
        return sqlSession.selectOne("existsBySNum", o_sNumber);
    }

    public void insertOwner(OwnerBean owner) {
        sqlSession.insert("insertOwner", owner);
    }
}
