package com.douzone.final_backend.service.impl;

import com.douzone.final_backend.vo.MasterVO;
import com.douzone.final_backend.vo.OwnerVO;
import com.douzone.final_backend.vo.UserVO;
import com.douzone.final_backend.DAO.MasterDAO;
import com.douzone.final_backend.DAO.OwnerDAO;
import com.douzone.final_backend.DTO.BannerDTO;
import com.douzone.final_backend.DTO.LocalDTO;
import com.douzone.final_backend.DTO.MasterDTO;
import com.douzone.final_backend.DTO.SaleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MasterServiceImpl implements com.douzone.final_backend.service.MasterService {

    @Autowired
    private MasterDAO masterDAO;

    @Autowired
    private OwnerDAO ownerDAO;

    @Override public List<OwnerVO> findAll() {
        return masterDAO.findAll();
    }


    @Override public void requestOK(String o_sNumber) {
//        log.info("service : " + masterDAO.requestOK(o_sNumber));
        int status = ownerDAO.findByOwner(o_sNumber).getO_approval();

        if (status == 3) {
            throw new RuntimeException("입점 해지 신청 대기 중인 가게는 처리할 수 없습니다.");
        } else if (status == 4) {
            throw new RuntimeException("입점 해지 신청 완료된 가게는 처리할 수 없습니다.");
        }else if (masterDAO.requestOK(o_sNumber) == 0) {
            throw new RuntimeException("입점 신청 수락 처리 중 에러가 발생했습니다.");
        }
        masterDAO.requestOK(o_sNumber);
    }

    @Override public int requestNO(String o_sNumber) {
        int status = ownerDAO.findByOwner(o_sNumber).getO_approval();
        log.info("사업자 번호 : " + o_sNumber);
        log.info("현재  상태 " + status);

        if (status == 1 || status == 3) {
            throw new RuntimeException("입점 신청 승인 후에는 반려 처리 할 수 없습니다.");
        } else if (status == 4) {
            throw new RuntimeException("입점 해지 신청 완료된 가맹점은 처리할 수 없습니다.");
        }else if (masterDAO.requestNO(o_sNumber) == 0) {
            throw new RuntimeException("입점 신청 반려 처리 중 에러가 발생했습니다.");
        }
        return masterDAO.requestNO(o_sNumber);
    }

    @Override public MasterVO login(MasterDTO masterDTO) {
        final MasterVO masterVO = masterDAO.masterLogin(masterDTO);

        log.info("serviceMasterBean : " + masterVO);
        if (masterVO == null) {
            throw new RuntimeException("아이디 또는 비밀번호를 확인해주세요");
        }


        return masterVO;

    }

    @Override public List<UserVO> userAll() {
        return masterDAO.userAll();
    }


    @Override public List<OwnerVO> findApproval() {
        return masterDAO.findApproval();
    }

    @Override public List<OwnerVO> approvalCompletion() {
        return masterDAO.approvalCompletion();
    }

    @Override public List<OwnerVO> terminationWaiting() {
        return masterDAO.terminationWaiting();
    }

    @Override public void terminationOK(String o_sNumber) {
//        log.info("service : " + masterDAO.terminationOK(o_sNumber));
        int status = ownerDAO.findByOwner(o_sNumber).getO_approval();

        if(status != 3){
            throw new RuntimeException("해지 신청 대기 중일 때만 처리가 가능합니다.");
        }
        if (masterDAO.terminationOK(o_sNumber) == 0)
            throw new RuntimeException("해지 신청 수락 처리 중 에러가 발생했습니다.");
        masterDAO.terminationOK(o_sNumber);
    }

    @Override public List<OwnerVO> terminationCompletion() {
        return masterDAO.terminationCompletion();
    }

    @Override public void terminationCancle(String o_sNumber) {
//        log.info("service : " + masterDAO.terminationCancle(o_sNumber));
        int status = ownerDAO.findByOwner(o_sNumber).getO_approval();
        if(status != 4){
            throw new RuntimeException("해지 신청 완료 되었을 때만 승인 취소 가능합니다.");
        }else if (masterDAO.terminationCancle(o_sNumber) == 0)
            throw new RuntimeException("해지 신청 취소 처리 중 에러가 발생하였습니다.");

        masterDAO.terminationCancle(o_sNumber);
    }

    @Override public List<BannerDTO> getBanner() {
        return masterDAO.getBanner();
    }

    @Override public int deleteBannerTable() {
        return masterDAO.deleteBannerTable();
    }

    @Override public int insertBannerTable(BannerDTO bannerDTO) {
        return masterDAO.insertBannerTable(bannerDTO);
    }

    @Override public List<SaleDTO> masterMonth(int dal) {
        return masterDAO.masterMonth(dal);
    }

    @Override public List<SaleDTO> masterYear(int nowYear) {
        return masterDAO.masterYear(nowYear);
    }

    @Override public List<SaleDTO> userMonth(int dal) {
        return masterDAO.userMonth(dal);
    }

    @Override public List<SaleDTO> userYear(int nowYear) {
        return masterDAO.userYear(nowYear);
    }

    @Override public List<SaleDTO> ownerUser(int dal) {
        return masterDAO.ownerUser(dal);
    }

    @Override public List<SaleDTO> onnerUserYear(int nowYear) {
        return masterDAO.onnerUserYear(nowYear);
    }

    @Override public List<SaleDTO> ownerUser2(int dal) {
        return masterDAO.ownerUser2(dal);
    }

    @Override public List<SaleDTO> onnerUserYear2(int nowYear) {
        return masterDAO.onnerUserYear2(nowYear);
    }

    @Override public LocalDTO OwnerUserChart3() {
        return masterDAO.OwnerUserChart3();
    }
}
