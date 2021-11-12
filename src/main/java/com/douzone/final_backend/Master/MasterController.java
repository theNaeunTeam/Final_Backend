package com.douzone.final_backend.Master;

import com.douzone.final_backend.Owner.OwnerBean;
import com.douzone.final_backend.security.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//이재현
@Slf4j
@RestController
@RequestMapping("/master")
public class MasterController {
    @Autowired
    private MasterService masterService;

    @GetMapping
    public ResponseEntity<?> getOwnerList() {

       try{
           List<OwnerBean> requestList = masterService.findAll();
           log.info("가게 정보 불러오기 성공");
           return ResponseEntity.ok().body(requestList);
       }catch (Exception e){
           ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
           return ResponseEntity
                   .badRequest()
                   .body(responseDTO);

       }

    }

}
