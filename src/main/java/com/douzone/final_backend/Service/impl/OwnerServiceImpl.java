package com.douzone.final_backend.service.impl;

import com.douzone.final_backend.vo.GoodsVO;
import com.douzone.final_backend.vo.OwnerVO;
import com.douzone.final_backend.vo.ReserveVO;
import com.douzone.final_backend.DAO.OwnerDAO;
import com.douzone.final_backend.DAO.UserDAO;
import com.douzone.final_backend.DTO.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class OwnerServiceImpl implements com.douzone.final_backend.service.OwnerService {
    @Autowired
    private OwnerDAO ownerDAO;

    @Autowired
    private UserDAO userDAO;

    // 로그인
    @Override
    public OwnerVO getByCredentials(String o_sNumber, String o_pw, PasswordEncoder passwordEncoder) {
        // 해당 사업자 번호로 정보 가져옴. 가맹점 상태 1 : 입점 승은 완료, 3 : 해지신청대기중 일 때만
        final OwnerVO originalOwner = ownerDAO.findBySNum(o_sNumber);

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
    @Override
    public int create(OwnerVO owner) {

        if (owner == null || owner.getO_sNumber() == null || owner.getO_pw() == null || owner.getO_address() == null || owner.getO_phone() == null || owner.getO_cellPhone() == null || owner.getO_image() == null || owner.getO_name() == null || owner.getO_time1() == null || owner.getO_time2() == null || owner.getO_latitude() == null || owner.getO_longitude() == null) {
            log.warn("owner 정보 누락");
            throw new RuntimeException("데이터가 누락되었습니다.");
        }
        final String o_sNumber = owner.getO_sNumber();
        // 존재 여부 확인. 반환 타입 true, false
        if (ownerDAO.existsBySNum(o_sNumber)||userDAO.existsById(o_sNumber)) {
            log.warn("이미 존재하는 사업자 번호");
            throw new RuntimeException("이미 존재하는 사업자 번호입니다.");
        }
        int result = ownerDAO.insertOwner(owner);

        return result;
    }

    @Override
    public int addGoods(GoodsVO goodsVO) {
        if (goodsVO.getG_name() == null || goodsVO.getG_count() == 0 || goodsVO.getG_price() == 0 || goodsVO.getG_discount() == 0 || goodsVO.getG_expireDate() == null || goodsVO.getG_category() == null) {
            log.warn("Goods 데이터 누락");
            throw new RuntimeException("필수 데이터가 누락되었습니다.");
        }

        int result = ownerDAO.addGoods(goodsVO);
        if (result == 0) {
            throw new RuntimeException("상픔 등록 실패했습니다.");
        }

        return result;
    }

    @Override
    public int updateGoods(GoodsVO goodsVO) {
        if (goodsVO.getG_name() == null || goodsVO.getG_count() == 0 || goodsVO.getG_price() == 0 || goodsVO.getG_discount() == 0 || goodsVO.getG_expireDate() == null || goodsVO.getG_category() == null) {
            log.warn("Goods Update 데이터 누락");
            throw new RuntimeException("필수 데이터가 누락되었습니다.");
        }
        int result = ownerDAO.updateGoods(goodsVO);
        if (result == 0) {
            throw new RuntimeException("상품 수정 실패했습니다.");
        }

        return result;
    }

    @Override
    public List<GoodsVO> goodsList(String o_sNumber) {

        if (o_sNumber == null) {
            throw new RuntimeException("필수 데이터가 누락되었습니다.");
        }
        return ownerDAO.goodsList(o_sNumber);
    }

    @Override
    @Transactional
    public void reserveCheck(ReserveDTO reserveDTO) {
        int check = reserveDTO.getCheck();
        if (check == 0) {
            throw new RuntimeException("상태를 선택해주세요.");
        }
        // 예약 승인
        else if (check == 1) {
            if (reserveDTO.getR_status() == 0) {
                int r = ownerDAO.resOK(reserveDTO);
                if (r == 0) {
                    throw new RuntimeException("승인 작업 중에 에러가 발생했습니다.");
                }
            } else {
                throw new RuntimeException("예약 대기 중 일 때만 승인할 수 있습니다.");
            }

        } else if (check == 2) { // 예약 거절
            if (reserveDTO.getR_status() == 0) {
                int r = ownerDAO.resNoCount(reserveDTO);
                int r1 = ownerDAO.resNo(reserveDTO);
                int r2 = ownerDAO.reNoSt(reserveDTO);
                if (r == 0 || r1 == 0 || r2 == 0) {
                    throw new RuntimeException("예약 거절 작업 중에 에러가 발생했습니다.");
                }
            } else {
                throw new RuntimeException("예약 대기 중 일 때만 거절할 수 있습니다.");
            }
        } else if (check == 3) { // 판매 완료
            if (reserveDTO.getR_status() == 1) {
                int r = ownerDAO.resSu(reserveDTO);
                int r1 = ownerDAO.point(reserveDTO);
                if (r == 0 || r1 == 0) {
                    throw new RuntimeException("판매완료 처리 중 에러가 발생했습니다.");
                }
            } else {
                throw new RuntimeException("예약 승인 되어있을 때만 판매 완료로 바꿀 수 있습니다.");
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
                    throw new RuntimeException("노쇼 작업 처리 중에 에러가 발생했습니다.");
                }
                ownerDAO.noShowCheck(reserveDTO);
            } else {
                throw new RuntimeException("예약 승인 되어있을 때만 노쇼로 등록 가능합니다.");
            }
        }

    }

    @Override
    public List<GoodsVO> allGoodsList() {
        return ownerDAO.allGoodList();
    }


    @Override
    public int deleteGoods(int g_code) {
        int result = ownerDAO.deleteGoods(g_code);
        if (g_code == 0) {
            throw new RuntimeException("데이터가 누락되었습니다.");
        } else if (result == 0) {
            throw new RuntimeException("상품 판매 완료 처리 실패하였습니다.");
        }
        return result;
    }

    @Override
    public List<ReserveVO> reserveListAll(String g_owner) {

        return ownerDAO.reserveListAll(g_owner);
    }

    @Override
    public List<ReserveVO> reserveList(String g_owner) {

        return ownerDAO.reserveList(g_owner);
    }

    @Override
    public GoodsVO goodsData(int r_g_code) {

        if (r_g_code == 0) {
            throw new RuntimeException("필수 데이터가 누락되었습니다.");
        } else if (ownerDAO.goodsData(r_g_code) == null) {
            throw new RuntimeException("데이터를 불러오는데 에러가 발생했습니다.");
        }
        return ownerDAO.goodsData(r_g_code);
    }

    @Override
    public ReserveVO reserveOne(ReserveDTO reserve) {
        if (reserve.getR_code() == 0) {
            throw new RuntimeException("필수 데이터가 누락되었습니다.");
        } else if (ownerDAO.reserveOne(reserve) == null) {
            throw new RuntimeException("데이터를 불러오는데 에러가 발생했습니다.");
        }
        return ownerDAO.reserveOne(reserve);
    }

    @Override
    public List<ReserveDTO> searchReserve(ReserveDTO reserveDTO) {
        if (reserveDTO.getR_status() == 9999) {
            return ownerDAO.searchReserveStatus(reserveDTO);

        } else {
            return ownerDAO.searchReserve(reserveDTO);
        }
    }

    @Override
    public List<GoodsDTO> search(GoodsDTO g) {
        if (g.getG_status() == 9999) {
            return ownerDAO.search(g);
        } else {
            return ownerDAO.searchStatus(g);
        }
    }

    @Override
    public OwnerPageDTO getOwner(String o_sNumber) {
        return ownerDAO.getOwner(o_sNumber);
    }

    // 등록한 상품 수
    @Override
    public int goods(String o_sNumber) {
        return ownerDAO.goods(o_sNumber);
    }

    @Override
    public int total(String o_sNumber) {
        return ownerDAO.total(o_sNumber);
    }

    @Override
    public int reserve(String o_sNumber) {

        return ownerDAO.reserve(o_sNumber);
    }

    @Override
    public void changeStatus(int g_code) {
        ownerDAO.changeStatus(g_code);
    }

    @Override
    public void deleteStatus(int g_code) {
        ownerDAO.deleteStatus(g_code);
    }

    @Override
    public List<SaleDTO> getDay(OwnerPageDTO dto) {
        return ownerDAO.getDay(dto);
    }

    @Override
    public List<SaleDTO> getMon(OwnerPageDTO o_sNumber) {
        return ownerDAO.getMon(o_sNumber);
    }

    @Override
    public List<SaleDTO> getYear(String o_sNumber) {
        return ownerDAO.getYear(o_sNumber);
    }

    @Override
    public List<SaleDTO> getTime(String r_owner) {

        if (r_owner == null) throw new RuntimeException("필수 데이터가 누락되었습니다.");

        return ownerDAO.getTime(r_owner);
    }

    @Override
    public List<SaleDTO> getGender(String r_owner) {
        return ownerDAO.getGender(r_owner);
    }

    @Override
    public List<SaleDTO> getAge(String r_owner) {
        return ownerDAO.getAge(r_owner);
    }

    @Override
    public List<SaleDTO> getCategorySale(String r_owner) {
        return ownerDAO.getCategorySale(r_owner);
    }

    @Override
    public boolean pushToken(PushTokenDTO pushTokenDTO) {
        if (ownerDAO.tokenDupChk(pushTokenDTO.getToken()) == 0) {
            ownerDAO.insertPushToken(pushTokenDTO);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public SaleDTO getNoShow(String owner) {
        if (owner == null) {
            throw new RuntimeException("이용할 수 없는 서비스입니다.");
        }
        return ownerDAO.getNoShow(owner);
    }

    @Override
    public SaleDTO getCancel(String owner) {
        if (owner == null) {
            throw new RuntimeException("이용할 수 없는 서비스입니다.");
        }
        return ownerDAO.getCancel(owner);
    }

    @Override
    public SaleDTO getOver(String owner) {
        if (owner == null) {
            throw new RuntimeException("이용할 수 없는 서비스입니다.");
        }
        return ownerDAO.getOver(owner);
    }

    @Override
    public int ownerExit(String o_sNumber) {
        if (o_sNumber == null) {
            throw new RuntimeException("이용할 수 없는 서비스입니다.");
        }
        return ownerDAO.ownerExit(o_sNumber);
    }

    @Override
    public int getGoodsReserve(int g_code) {
        return ownerDAO.getGoodsReserve(g_code);
    }
}