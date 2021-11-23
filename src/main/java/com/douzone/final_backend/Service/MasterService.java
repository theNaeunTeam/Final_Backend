package com.douzone.final_backend.Service;

import com.douzone.final_backend.Bean.MasterBean;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.Bean.UserBean;
import com.douzone.final_backend.DAO.MasterDAO;
import com.douzone.final_backend.DTO.BannerDTO;
import com.douzone.final_backend.DTO.MasterDTO;
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
        if (masterBean != null) {

            return masterBean;
        }

        return null;
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

}
