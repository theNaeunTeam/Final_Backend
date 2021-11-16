package com.douzone.final_backend.Service;

import com.douzone.final_backend.Bean.GoodsBean;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.DAO.OwnerDAO;
import com.douzone.final_backend.DTO.ReserveDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class OwnerService {
    @Autowired
    private OwnerDAO ownerDAO;


    public OwnerBean getByCredentials(String o_sNumber, String o_pw, PasswordEncoder passwordEncoder) {
        final OwnerBean originalOwner = ownerDAO.findBySNum(o_sNumber);

        if (originalOwner != null && passwordEncoder.matches(o_pw, originalOwner.getO_pw())) {
            log.info("originalOwner : " + originalOwner);
            return originalOwner;
        }
        return null;

    }

    public OwnerBean create(OwnerBean owner) {
        if (owner == null || owner.getO_sNumber() == null) {
            log.warn("owner 정보 누락");
            throw new RuntimeException("owner 데이터 누락");
        }
        final String o_sNumber = owner.getO_sNumber();

        if (ownerDAO.existsBySNum(o_sNumber)) {
            log.warn("이미 존재하는 사업자 번호");
            throw new RuntimeException("이미 존재하는 사업자 번호");
        }
        ownerDAO.insertOwner(owner);

        return owner;
    }

    public GoodsBean addGoods(GoodsBean goodsBean) {
        if (goodsBean == null) {
            log.warn("Goods 데이터 누락");
            throw new RuntimeException("Goods 데이터 누락");
        }

        int result = ownerDAO.addGoods(goodsBean);
        if (result != 0) {
            throw new RuntimeException("결과값 안나옴");
        }
        return goodsBean;


    }

    public GoodsBean updateGoods(GoodsBean goodsBean) {
        if (goodsBean == null) {
            log.warn("Goods Update 데이터 누락");
            throw new RuntimeException("Goods Update 데이터 누락");
        }
        int result = ownerDAO.updateGoods(goodsBean);
        if (result != 0) {
            throw new RuntimeException("결과값 안나옴");
        }
        return goodsBean;


    }

    public List<GoodsBean> goodsList(String o_sNumber) {

        if (ownerDAO.goodsList(o_sNumber) == null) {
            throw new RuntimeException("goodsList 결과값 없음 에러");
        }
        return ownerDAO.goodsList(o_sNumber);
    }

    @Transactional
    public void reserveCheck(ReserveDTO reserveDTO) throws Exception {
        String check = reserveDTO.getCheck();
        if (check.equals("승인")) {
            ownerDAO.resOK(reserveDTO);
        } else if (check.equals("거절")) {
            ownerDAO.resNoCount(reserveDTO);
            ownerDAO.resNo(reserveDTO);
            ownerDAO.reNoSt(reserveDTO);
        } else if (check.equals("판매완료")) {
            ownerDAO.resSu(reserveDTO);
        } else if (check.equals("노쇼")) {
            ownerDAO.resNoCount(reserveDTO);
            ownerDAO.reseNoShowStatus(reserveDTO);
            ownerDAO.resNoShowCount(reserveDTO);
            ownerDAO.resNSSt(reserveDTO);
        }

    }


}