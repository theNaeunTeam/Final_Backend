package com.douzone.final_backend.DAO;

import com.douzone.final_backend.Bean.MasterBean;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.Bean.UserBean;
import com.douzone.final_backend.DTO.BannerDTO;
import com.douzone.final_backend.DTO.MasterDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class MasterDAO {

    @Autowired
    private SqlSession sqlSession;


    public List<OwnerBean> findAll() {
        return sqlSession.selectList("findAll");
    }

    public int requestOK(String o_sNumber) {
        log.info("master : " + sqlSession.update("requestOK", o_sNumber));
        return sqlSession.update("requestOK", o_sNumber);
    }

    public int requestNO(String o_sNumber) {
        return sqlSession.update("requestNO", o_sNumber);
    }

    public MasterDTO findByMaster(String id) {
        return sqlSession.selectOne("findByMaster", id);
    }

    public MasterBean masterLogin(MasterDTO masterDTO) {
        log.info(masterDTO.getM_id() + "/" + masterDTO.getM_pw());
        log.info("MasterBean  : " + sqlSession.selectOne("masterlogin", masterDTO));
        return sqlSession.selectOne("masterlogin", masterDTO);
    }

    public List<UserBean> userAll() {
        return sqlSession.selectList("userAll");
    }

    public List<BannerDTO> getBanner(){
        return sqlSession.selectList("getBanner");
    }
}
