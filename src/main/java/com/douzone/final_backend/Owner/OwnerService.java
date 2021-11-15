package com.douzone.final_backend.Owner;

import com.douzone.final_backend.Goods.GoodsBean;
import com.douzone.final_backend.Reserve.ReserveDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        ownerDAO.addGoods(goodsBean);

        return goodsBean;

    }

    public List<GoodsBean> goodsList(String o_sNumber) {

        return ownerDAO.goodsList(o_sNumber);
    }

    public int reserveCheck(ReserveDTO reserveDTO){

        return ownerDAO.reserveCheck(reserveDTO);
    }

}