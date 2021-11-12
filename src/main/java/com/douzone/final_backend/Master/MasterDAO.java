package com.douzone.final_backend.Master;

import com.douzone.final_backend.Owner.OwnerBean;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MasterDAO {

    @Autowired
    private SqlSession sqlSession;


    public List<OwnerBean> findAll() {
        return sqlSession.selectList("findAll");
    }

    public int requestOK(int o_sNumber) {
        return sqlSession.update("requestOK",o_sNumber);
    }
}
