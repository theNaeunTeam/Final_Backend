package com.douzone.final_backend.Master;

import com.douzone.final_backend.Owner.OwnerBean;
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
        log.info("service : "+masterDAO.requestOK(o_sNumber));
        return masterDAO.requestOK(o_sNumber);
    }
}
