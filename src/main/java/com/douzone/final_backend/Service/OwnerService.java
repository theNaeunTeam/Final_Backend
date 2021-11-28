package com.douzone.final_backend.Service;

import com.douzone.final_backend.Bean.GoodsBean;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.Bean.ReserveBean;
import com.douzone.final_backend.DAO.OwnerDAO;
import com.douzone.final_backend.DTO.*;
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

    // 로그인
    public OwnerBean getByCredentials(String o_sNumber, String o_pw, PasswordEncoder passwordEncoder) {
        // 해당 사업자 번호로 정보 가져옴. 가맹점 상태 1 : 입점 승은 완료, 3 : 해지신청대기중 일 때만
        final OwnerBean originalOwner = ownerDAO.findBySNum(o_sNumber);

        // 해당 사업자 번호로 조회가 안될 때 예외 발생
        if (originalOwner == null) {
            throw new RuntimeException("서비스 이용중인 사업자 번호가 아닙니다.");
        } // 입력한 비밀번호와 암호화된 비밀번호 비교 후 틀리면 예외 발생
        else if (passwordEncoder.matches(o_pw, originalOwner.getO_pw()) == false) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

        return originalOwner;

    }
    // 입점신청
    public int create(OwnerBean owner) {

        if (owner == null || owner.getO_sNumber() == null) {
            log.warn("owner 정보 누락");
            throw new RuntimeException("데이터가 누락되었습니다.");
        }
        final String o_sNumber = owner.getO_sNumber();
        // 존재 여부 확인. 반환 타입 true, false
        if (ownerDAO.existsBySNum(o_sNumber)) {
            log.warn("이미 존재하는 사업자 번호");
            throw new RuntimeException("이미 존재하는 사업자 번호입니다.");
        }
        int result = ownerDAO.insertOwner(owner);

        return result;
    }

    public int addGoods(GoodsBean goodsBean) {
        if (goodsBean == null) {
            log.warn("Goods 데이터 누락");
            throw new RuntimeException("데이터 누락");
        }

        int result = ownerDAO.addGoods(goodsBean);
        if (result == 0) {
            throw new RuntimeException("상픔 등록 실패했습니다.");
        }


        return result;
    }

    public int updateGoods(GoodsBean goodsBean) {
        if (goodsBean == null) {
            log.warn("Goods Update 데이터 누락");
            throw new RuntimeException("데이터 누락");
        }
        int result = ownerDAO.updateGoods(goodsBean);
        if (result == 0) {
            throw new RuntimeException("상품 수정 실패했습니다.");
        }


        return result;
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
        // 예약 승인
        if (check == 1) {
            if (reserveDTO.getR_status() == 0) {
                int r = ownerDAO.resOK(reserveDTO);
                if (r == 0) {
                    throw new RuntimeException("승인 에러");
                }
            } else {
                throw new RuntimeException("예약 대기 중 일 때만 승인할 수 있음 r_status : " + reserveDTO.getR_status());
            }

        } else if (check == 2) { // 예약 거절
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
        } else if (check == 3) { // 판매 완료
            if (reserveDTO.getR_status() == 1) {
                int r = ownerDAO.resSu(reserveDTO);
                int r1 = ownerDAO.point(reserveDTO);
                if (r == 0 || r1 == 0) {
                    throw new RuntimeException("판매완료 에러");
                }
            } else {
                throw new RuntimeException("예약 승인 되어있을 때만 판매 완료로 바꿀 수 있음 r_status : " + reserveDTO.getR_status());
            }

        } else if (check == 4) { // 노쇼
            if (reserveDTO.getR_status() == 1) {
                int r = ownerDAO.resNoCount(reserveDTO);
                int r1 = ownerDAO.reseNoShowStatus(reserveDTO);
                int r2 = ownerDAO.resNoShowCount(reserveDTO);
                int r3 = ownerDAO.reNoSt(reserveDTO);
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


    public int deleteGoods(int g_code) {
        int result = ownerDAO.deleteGoods(g_code);
        if(g_code == 0){
         throw new RuntimeException("데이터가 누락되었습니다.");
        }else if(result == 1){
            throw new RuntimeException("상품 삭제 실패하였습니다.");
        }
        return result;
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

    public OwnerPageDTO getOwner(String o_sNumber) {
        return ownerDAO.getOwner(o_sNumber);
    }

    // 등록한 상품 수
    public int goods(String o_sNumber) {
        return ownerDAO.goods(o_sNumber);
    }

    public int total(String o_sNumber) {
        return ownerDAO.total(o_sNumber);
    }

    public int reserve(String o_sNumber) {

        return ownerDAO.reserve(o_sNumber);
    }

    public void changeStatus(int g_code) {
        ownerDAO.changeStatus(g_code);
    }

    public void deleteStatus(int g_code) {
        ownerDAO.deleteStatus(g_code);
    }

    public List<SaleDTO> getDay(OwnerPageDTO dto) {
        return ownerDAO.getDay(dto);
    }

    public List<SaleDTO> getMon(OwnerPageDTO o_sNumber) {
        return ownerDAO.getMon(o_sNumber);
    }

    public List<SaleDTO> getYear(String o_sNumber) {
        return ownerDAO.getYear(o_sNumber);
    }

    public List<SaleDTO> getTime(String r_owner) {

        if (r_owner == null) throw new RuntimeException("들어온 값 없음");

        return ownerDAO.getTime(r_owner);
    }

    public List<SaleDTO> getGender(String r_owner) {
        return ownerDAO.getGender(r_owner);
    }

    public List<SaleDTO> getAge(String r_owner) {
        return ownerDAO.getAge(r_owner);
    }

    public List<SaleDTO> getCategorySale(String r_owner) {
        return ownerDAO.getCategorySale(r_owner);
    }

    public boolean pushToken(PushTokenDTO pushTokenDTO) {
        if (ownerDAO.tokenDupChk(pushTokenDTO.getToken()) == 0) {
            ownerDAO.insertPushToken(pushTokenDTO);
            return true;
        } else {
            return false;
        }
    }

    public SaleDTO getNoShow(String owner) {
        return ownerDAO.getNoShow(owner);
    }

    public SaleDTO getCancel(String owner) {
        return ownerDAO.getCancel(owner);
    }

    public SaleDTO getOver(String owner) {
        return ownerDAO.getOver(owner);
    }

    public int ownerExit(String o_sNumber) {
        return ownerDAO.ownerExit(o_sNumber);
    }

    public int getGoodsReserve(int g_code) {
        return ownerDAO.getGoodsReserve(g_code);
    }
}