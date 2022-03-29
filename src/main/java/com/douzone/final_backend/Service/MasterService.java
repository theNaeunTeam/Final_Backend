package com.douzone.final_backend.service;

import com.douzone.final_backend.vo.MasterVO;
import com.douzone.final_backend.vo.OwnerVO;
import com.douzone.final_backend.vo.UserVO;
import com.douzone.final_backend.DTO.BannerDTO;
import com.douzone.final_backend.DTO.LocalDTO;
import com.douzone.final_backend.DTO.MasterDTO;
import com.douzone.final_backend.DTO.SaleDTO;

import java.util.List;

public interface MasterService {
    List<OwnerVO> findAll();

    void requestOK(String o_sNumber);

    int requestNO(String o_sNumber);

    MasterVO login(MasterDTO masterDTO);

    List<UserVO> userAll();

    List<OwnerVO> findApproval();

    List<OwnerVO> approvalCompletion();

    List<OwnerVO> terminationWaiting();

    void terminationOK(String o_sNumber);

    List<OwnerVO> terminationCompletion();

    void terminationCancle(String o_sNumber);

    List<BannerDTO> getBanner();

    int deleteBannerTable();

    int insertBannerTable(BannerDTO bannerDTO);

    List<SaleDTO> masterMonth(int dal);

    List<SaleDTO> masterYear(int nowYear);

    List<SaleDTO> userMonth(int dal);

    List<SaleDTO> userYear(int nowYear);

    List<SaleDTO> ownerUser(int dal);

    List<SaleDTO> onnerUserYear(int nowYear);

    List<SaleDTO> ownerUser2(int dal);

    List<SaleDTO> onnerUserYear2(int nowYear);

    LocalDTO OwnerUserChart3();
}
