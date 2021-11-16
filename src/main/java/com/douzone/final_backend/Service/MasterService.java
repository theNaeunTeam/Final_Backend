package com.douzone.final_backend.Service;

import com.douzone.final_backend.Bean.MasterBean;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.Bean.UserBean;
import com.douzone.final_backend.DAO.MasterDAO;
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


    public int requestOK(String o_sNumber) {
        log.info("service : " + masterDAO.requestOK(o_sNumber));
        return masterDAO.requestOK(o_sNumber);
    }

    public int requestNO(String o_sNumber) {
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
}