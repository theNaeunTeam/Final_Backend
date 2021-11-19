package com.douzone.final_backend.Service;

import com.douzone.final_backend.Bean.GoodsBean;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.Bean.ReserveBean;
import com.douzone.final_backend.DAO.OwnerDAO;
import com.douzone.final_backend.DTO.GoodsDTO;
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
        if (result == 0) {
            throw new RuntimeException("인서트 실패 포링키 확인할것");
        }


        return goodsBean;
    }

    public GoodsBean updateGoods(GoodsBean goodsBean) {
        if (goodsBean == null) {
            log.warn("Goods Update 데이터 누락");
            throw new RuntimeException("Goods Update 데이터 누락");
        }
        int result = ownerDAO.updateGoods(goodsBean);
        if (result == 0) {
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
    public void reserveCheck(ReserveDTO reserveDTO) {
        int check = reserveDTO.getCheck();
        if (check == 1) {
            if (reserveDTO.getR_status() == 0) {
                int r = ownerDAO.resOK(reserveDTO);
                if (r == 0) {
                    throw new RuntimeException("승인 에러");
                }
            } else {
                throw new RuntimeException("예약 대기 중 일 때만 승인할 수 있음 r_status : " + reserveDTO.getR_status());
            }

        } else if (check == 2) {
            if (reserveDTO.getR_status() == 0) {
                int r = ownerDAO.resNoCount(reserveDTO);
                int r1 = ownerDAO.resNo(reserveDTO);
                int r2 = ownerDAO.reNoSt(reserveDTO);
                if (r == 0 || r1 == 0 || r2 == 0) {
                    throw new RuntimeException("예약 거절 에러");
                }
            } else {
                throw new RuntimeException("예약 대기 중 일 때만 거절할 수 있음 r_status : " + reserveDTO.getR_status());
            }
        } else if (check == 3) {
            if (reserveDTO.getR_status() == 1) {
                int r = ownerDAO.resSu(reserveDTO);
                if (r == 0) {
                    throw new RuntimeException("판매완료 에러");
                }
            } else {
                throw new RuntimeException("예약 승인 되어있을 때만 판매 완료로 바꿀 수 있음 r_status : " + reserveDTO.getR_status());
            }

        } else if (check == 4) {
            if (reserveDTO.getR_status() == 1) {
                int r = ownerDAO.resNoCount(reserveDTO);
                int r1 = ownerDAO.reseNoShowStatus(reserveDTO);
                int r2 = ownerDAO.resNoShowCount(reserveDTO);
                int r3 = ownerDAO.resNSSt(reserveDTO);
                log.info("r_code" + reserveDTO.getR_code());
                log.info("" + r + "/" + r1 + "/" + r2 + "/" + r3);
                if (r == 0 || r1 == 0 || r2 == 0 || r3 == 0) {
                    log.error("노쇼 에러" + r, r1, r2, r3);
                    throw new RuntimeException("노쇼 에러");
                }
                ownerDAO.noShowCheck(reserveDTO);
            } else {
                throw new RuntimeException("예약 승인 되어있을 때만 노쇼로 등록 가능 r_status : " + reserveDTO.getR_status());
            }


        }

    }


    public List<GoodsBean> allGoodsList() {
        return ownerDAO.allGoodList();
    }

    public void changeStatus(int g_code) {
        ownerDAO.changeStatus(g_code);
    }

    public void deleteGoods(GoodsDTO goodsDTO) {
        int r = ownerDAO.deleteGoods(goodsDTO);
    }

    public List<ReserveBean> reserveList(String g_owner) {

        return ownerDAO.reserveList(g_owner);
    }


    public GoodsBean goodsData(int r_g_code) {

        if (ownerDAO.goodsData(r_g_code) == null) {
            throw new RuntimeException("반환되는 값 없음");
        }
        return ownerDAO.goodsData(r_g_code);
    }

    public ReserveBean reserveOne(ReserveDTO reserve) {
        if (ownerDAO.reserveOne(reserve) == null) {
            throw new RuntimeException("반환되는 값 없음");
        }
        return ownerDAO.reserveOne(reserve);
    }

    public List<ReserveDTO> searchReserve(ReserveDTO reserveDTO) {
        if (reserveDTO.getR_status() == 9999) {
            return ownerDAO.searchReserveStatus(reserveDTO);

        } else {
            return ownerDAO.searchReserve(reserveDTO);
        }
    }

    public List<GoodsDTO> search(GoodsDTO g) {
        if (g.getG_status() == 9999) {
            return ownerDAO.search(g);
        } else {
            return ownerDAO.searchStatus(g);
        }
    }
}