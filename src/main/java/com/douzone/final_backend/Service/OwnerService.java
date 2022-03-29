package com.douzone.final_backend.service;

import com.douzone.final_backend.vo.GoodsVO;
import com.douzone.final_backend.vo.OwnerVO;
import com.douzone.final_backend.vo.ReserveVO;
import com.douzone.final_backend.DTO.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OwnerService {
    // 로그인
    OwnerVO getByCredentials(String o_sNumber, String o_pw, PasswordEncoder passwordEncoder);

    // 입점신청
    int create(OwnerVO owner);

    int addGoods(GoodsVO goodsVO);

    int updateGoods(GoodsVO goodsVO);

    List<GoodsVO> goodsList(String o_sNumber);

    @Transactional
    void reserveCheck(ReserveDTO reserveDTO);

    List<GoodsVO> allGoodsList();

    int deleteGoods(int g_code);

    List<ReserveVO> reserveListAll(String g_owner);

    List<ReserveVO> reserveList(String g_owner);

    GoodsVO goodsData(int r_g_code);

    ReserveVO reserveOne(ReserveDTO reserve);

    List<ReserveDTO> searchReserve(ReserveDTO reserveDTO);

    List<GoodsDTO> search(GoodsDTO g);

    OwnerPageDTO getOwner(String o_sNumber);

    // 등록한 상품 수
    int goods(String o_sNumber);

    int total(String o_sNumber);

    int reserve(String o_sNumber);

    void changeStatus(int g_code);

    void deleteStatus(int g_code);

    List<SaleDTO> getDay(OwnerPageDTO dto);

    List<SaleDTO> getMon(OwnerPageDTO o_sNumber);

    List<SaleDTO> getYear(String o_sNumber);

    List<SaleDTO> getTime(String r_owner);

    List<SaleDTO> getGender(String r_owner);

    List<SaleDTO> getAge(String r_owner);

    List<SaleDTO> getCategorySale(String r_owner);

    boolean pushToken(PushTokenDTO pushTokenDTO);

    SaleDTO getNoShow(String owner);

    SaleDTO getCancel(String owner);

    SaleDTO getOver(String owner);

    int ownerExit(String o_sNumber);

    int getGoodsReserve(int g_code);
}
