package com.douzone.final_backend.Master;

import com.douzone.final_backend.Common.ResponseDTO;
import com.douzone.final_backend.Owner.OwnerBean;
import com.douzone.final_backend.Owner.OwnerDTO;
import com.douzone.final_backend.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.List;

//이재현
@Slf4j
@RestController
@RequestMapping("/master")
public class MasterController {
    @Autowired
    private MasterService masterService;
    @Autowired
    private TokenProvider tokenProvider;

    @GetMapping
    public ResponseEntity<?> getOwnerList() {

        try {
            List<OwnerBean> requestList = masterService.findAll();
            log.info("가게 정보 불러오기 성공");
            return ResponseEntity.ok().body(requestList);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);

        }

    }

    @PostMapping("/requestOK")
    public ResponseEntity<?> ownerRequestCheck(@RequestBody OwnerDTO ownerDTO) {
        log.info("sNumber : " + ownerDTO.getO_sNumber());
        try {
            int requestCheck = masterService.requestOK(ownerDTO.getO_sNumber());
            log.info("check : " + requestCheck);
            return ResponseEntity.ok().body(requestCheck);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }


    // 마스터 로그인 true, false
    @PostMapping("masterlogin")
    public ResponseEntity<?> masterLogin(@RequestBody MasterDTO masterDTO) {
        log.info("masterDTO : " + masterDTO);
        MasterBean master = masterService.login(masterDTO);
//        MasterDTO master = masterService.findMaster(masterDTO);
        log.info("master ??" + master);
        if (master != null) {
            final String id = master.getM_id() + "&NASTER";
            final String token = tokenProvider.create(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            headers.add("X-AUTH-TOKEN", token);

            return new ResponseEntity<>(true, headers, HttpStatus.OK);

        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("masterLoing Failed")
                    .build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }


}
