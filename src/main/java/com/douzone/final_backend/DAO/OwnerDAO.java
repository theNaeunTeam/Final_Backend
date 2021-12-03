package com.douzone.final_backend.DAO;

import com.douzone.final_backend.Bean.GoodsBean;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.Bean.ReserveBean;
import com.douzone.final_backend.DTO.*;
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

    public int insertOwner(OwnerBean owner) {
        return sqlSession.insert("insertOwner", owner);
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

    public int point(ReserveDTO reserveDTO) {
        return sqlSession.update("point", reserveDTO);
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


    public int deleteGoods(int g_code) {
        return sqlSession.update("deleteGoods", g_code);
    }

    public List<ReserveBean> reserveListAll(String g_owner) {

        return sqlSession.selectList("reserveListAll", g_owner);
    }

    public GoodsBean goodsData(int r_g_code) {
        return sqlSession.selectOne("goodsData", r_g_code);
    }

    public ReserveBean reserveOne(ReserveDTO reserve) {
        return sqlSession.selectOne("reserveOne", reserve);
    }

    public List<ReserveDTO> searchReserve(ReserveDTO reserveDTO) {
        return sqlSession.selectList("searchReserve", reserveDTO);
    }

    public List<ReserveDTO> searchReserveStatus(ReserveDTO reserveDTO) {
        return sqlSession.selectList("searchReserveStatus", reserveDTO);
    }

    public List<GoodsDTO> search(GoodsDTO g) {
        return sqlSession.selectList("search", g);
    }

    public List<GoodsDTO> searchStatus(GoodsDTO g) {
        return sqlSession.selectList("searchStatus", g);
    }

    public OwnerPageDTO getOwner(String o_sNumber) {
        return sqlSession.selectOne("getOwner", o_sNumber);
    }

    public int goods(String o_sNumber) {
        return sqlSession.selectOne("goods", o_sNumber);
    }

    public int total(String o_sNumber) {
        return sqlSession.selectOne("total", o_sNumber);
    }

    public int reserve(String o_sNumber) {
        return sqlSession.selectOne("reserve", o_sNumber);
    }


    //스케줄러가 유통기한 지나면 상태 변환
    public void changeStatus(int g_code) {
        sqlSession.update("changeStatus", g_code);
    }

    public void deleteStatus(int g_code) {
        sqlSession.update("deleteStatus", g_code);
    }

    public List<SaleDTO> getDay(OwnerPageDTO dto) {
        return sqlSession.selectList("getDay", dto);
    }

    public List<SaleDTO> getMon(OwnerPageDTO o_sNumber) {
        return sqlSession.selectList("getMon", o_sNumber);
    }

    public List<SaleDTO> getYear(String o_sNumber) {
        return sqlSession.selectList("getYear", o_sNumber);
    }

    public List<SaleDTO> getTime(String r_owner) {
        return sqlSession.selectList("getTime", r_owner);
    }

    public List<SaleDTO> getGender(String r_owner) {
        return sqlSession.selectList("getGender", r_owner);
    }

    public List<SaleDTO> getAge(String r_owner) {
        return sqlSession.selectList("getAge", r_owner);
    }

    public List<SaleDTO> getCategorySale(String r_owner) {
        return sqlSession.selectList("getCategorySale", r_owner);
    }

    public int tokenDupChk(String token){
        return sqlSession.selectOne("tokenDupChk", token);
    }

    public int insertPushToken(PushTokenDTO pushTokenDTO){
        return sqlSession.insert("insertPushToken", pushTokenDTO);
    }

    public SaleDTO getNoShow(String owner) {
        return sqlSession.selectOne("getNoShow",owner);
    }

    public SaleDTO getCancel(String owner) {
        return sqlSession.selectOne("getCancel",owner);
    }

    public SaleDTO getOver(String owner) {
        return sqlSession.selectOne("getOver",owner);
    }

    public int ownerExit(String o_sNumber) {
        return sqlSession.update("ownerExit",o_sNumber);
    }

    public int getGoodsReserve(int g_code) {
        return sqlSession.selectOne("getGoodsReserve",g_code);
    }
}
