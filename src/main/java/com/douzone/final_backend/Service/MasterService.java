package com.douzone.final_backend.Service;

import com.douzone.final_backend.Bean.MasterBean;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.Bean.UserBean;
import com.douzone.final_backend.DAO.MasterDAO;
import com.douzone.final_backend.DTO.BannerDTO;
import com.douzone.final_backend.DTO.MasterDTO;
import com.douzone.final_backend.DTO.SaleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MasterService {

    @Autowired
    private MasterDAO masterDAO;

    public List<OwnerBean> findAll() {
        return masterDAO.findAll();
    }


    public void requestOK(String o_sNumber) {
        log.info("service : " + masterDAO.requestOK(o_sNumber));
        if (masterDAO.requestOK(o_sNumber) == 0)
            throw new RuntimeException("입점 신청 수락 에러");

        masterDAO.requestOK(o_sNumber);
    }

    public int requestNO(String o_sNumber) {
        if (masterDAO.requestNO(o_sNumber) == 0)
            throw new RuntimeException("입점 신청 거절 에러");
        return masterDAO.requestNO(o_sNumber);
    }

    public MasterBean login(MasterDTO masterDTO) {
        final MasterBean masterBean = masterDAO.masterLogin(masterDTO);

        log.info("serviceMasterBean : " + masterBean);
        if (masterBean == null) {
            throw new RuntimeException("아이디 또는 비밀번호를 확인해주세요");
        }


        return masterBean;

    }

    public List<UserBean> userAll() {
        return masterDAO.userAll();
    }


    public List<OwnerBean> findApproval() {
        return masterDAO.findApproval();
    }

    public List<OwnerBean> approvalCompletion() {
        return masterDAO.approvalCompletion();
    }

    public List<OwnerBean> terminationWaiting() {
        return masterDAO.terminationWaiting();
    }

    public void terminationOK(String o_sNumber) {
        log.info("service : " + masterDAO.terminationOK(o_sNumber));
        if (masterDAO.terminationOK(o_sNumber) == 0)
            throw new RuntimeException("해지 신청 수락 에러");

        masterDAO.terminationOK(o_sNumber);
    }

    public List<OwnerBean> terminationCompletion() {
        return masterDAO.terminationCompletion();
    }

    public void terminationCancle(String o_sNumber) {
        log.info("service : " + masterDAO.terminationCancle(o_sNumber));
        if (masterDAO.terminationCancle(o_sNumber) == 0)
            throw new RuntimeException("해지 반려 신청 수락 에러");

        masterDAO.terminationCancle(o_sNumber);
    }

    public List<BannerDTO> getBanner() {
        return masterDAO.getBanner();
    }

    public int deleteBannerTable() {
        return masterDAO.deleteBannerTable();
    }

    public int insertBannerTable(BannerDTO bannerDTO) {
        return masterDAO.insertBannerTable(bannerDTO);
    }

    public List<SaleDTO> masterMonth(int dal) {
        return masterDAO.masterMonth(dal);
    }

    public List<SaleDTO> masterYear(int nowYear) {
        return masterDAO.masterYear(nowYear);
    }

    public List<SaleDTO> userMonth(int dal) {
        return masterDAO.userMonth(dal);
    }

    public List<SaleDTO> userYear(int nowYear) {
        return masterDAO.userYear(nowYear);
    }

    public List<SaleDTO> ownerUser(int dal) {
        return masterDAO.ownerUser(dal);
    }

    public List<SaleDTO> onnerUserYear(int nowYear) {
        return masterDAO.onnerUserYear(nowYear);
    }

    public List<SaleDTO> ownerUser2(int dal) {
        return masterDAO.ownerUser2(dal);
    }

    public List<SaleDTO> onnerUserYear2(int nowYear) {
        return masterDAO.onnerUserYear2(nowYear);
    }


}
