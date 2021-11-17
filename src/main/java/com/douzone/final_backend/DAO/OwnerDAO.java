package com.douzone.final_backend.DAO;

import com.douzone.final_backend.Bean.GoodsBean;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.DTO.OwnerDTO;
import com.douzone.final_backend.DTO.ReserveDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Slf4j
@Transactional
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

    public OwnerDTO findByOwner(String id) {
        return sqlSession.selectOne("findByOwner", id);
    }

    public int addGoods(GoodsBean goodsBean) {
        return sqlSession.insert("addGoods", goodsBean);
    }

    public int updateGoods(GoodsBean goodsBean) {
        return sqlSession.update("updateGoods", goodsBean);
    }

    public List<GoodsBean> goodsList(String o_sNumber) {
        return sqlSession.selectList("goodsList", o_sNumber);
    }


    public void resOK(ReserveDTO reserveDTO) {
        sqlSession.update("resOK", reserveDTO);
    }


    public void resNoCount(ReserveDTO reserveDTO) {
        sqlSession.update("resNoCount", reserveDTO);
    }

    public void resNo(ReserveDTO reserveDTO) {
        sqlSession.update("resNo", reserveDTO);
    }

    public void reNoSt(ReserveDTO reserveDTO) {
        sqlSession.update("reNoSt", reserveDTO);
    }

    public void resSu(ReserveDTO reserveDTO) {
        sqlSession.update("resSu", reserveDTO);
    }

    public void reseNoShowStatus(ReserveDTO reserveDTO) {
        sqlSession.update("reseNoShowStatus", reserveDTO);
    }

    public void resNoShowCount(ReserveDTO reserveDTO) {
        sqlSession.update("resNoShowCount", reserveDTO);
    }

    public void resNSSt(ReserveDTO reserveDTO) {
        sqlSession.update("resNSSt", reserveDTO);
    }

    public List<GoodsBean> allGoodList() {
        return sqlSession.selectList("allGoodsList");
    }

    public void changeStatus(int g_code) {
        sqlSession.update("changeStatus", g_code);
    }
}
