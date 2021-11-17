package com.douzone.final_backend.DAO;

import com.douzone.final_backend.Bean.GoodsBean;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.DTO.GoodsDTO;
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

    // 예약 승인
    public int resOK(ReserveDTO reserveDTO) {
        return sqlSession.update("resOK", reserveDTO);
    }

    // 예약 거절
    public int resNoCount(ReserveDTO reserveDTO) {
        return sqlSession.update("resNoCount", reserveDTO);
    }
    public int resNo(ReserveDTO reserveDTO) {
        return sqlSession.update("resNo", reserveDTO);
    }
    public int reNoSt(ReserveDTO reserveDTO) {
        return sqlSession.update("reNoSt", reserveDTO);
    }

    // 판매 완료
    public int resSu(ReserveDTO reserveDTO) {
        return sqlSession.update("resSu", reserveDTO);
    }

    // 노쇼 . 예약 거절 resNoCount 재사용
    public int reseNoShowStatus(ReserveDTO reserveDTO) {
        return sqlSession.update("reseNoShowStatus", reserveDTO);
    }
    public int resNoShowCount(ReserveDTO reserveDTO) {
        return sqlSession.update("resNoShowCount", reserveDTO);
    }
    public int resNSSt(ReserveDTO reserveDTO) {
        return sqlSession.update("resNSSt", reserveDTO);
    }

    // noShowcnt 5이상인지 확인하고 5이상이면 블랙리스트
    public void noShowCheck(ReserveDTO reserveDTO) {
        sqlSession.update("noShowCheck", reserveDTO);
    }

    public List<GoodsBean> allGoodList() {
        return sqlSession.selectList("allGoodsList");
    }

    public void changeStatus(int g_code) {
        sqlSession.update("changeStatus", g_code);
    }

    public int deleteGoods(GoodsDTO goodsDTO) {
        return sqlSession.update("deleteGoods",goodsDTO);
    }
}
