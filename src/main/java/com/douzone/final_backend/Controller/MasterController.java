package com.douzone.final_backend.Controller;

import com.douzone.final_backend.Bean.MasterBean;
import com.douzone.final_backend.Common.ResponseDTO;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.DTO.OwnerDTO;
import com.douzone.final_backend.Bean.UserBean;
import com.douzone.final_backend.DTO.MasterDTO;
import com.douzone.final_backend.Service.MasterService;
import com.douzone.final_backend.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/master")
public class MasterController {
    @Autowired
    private MasterService masterService;
    @Autowired
    private TokenProvider tokenProvider;

    @GetMapping("/tokencheck")
    public String UserTokenCheck() {
        return "success";
    }

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

    @PatchMapping("/requestOK")
    public ResponseEntity<?> ownerRequestCheck(@RequestBody OwnerDTO ownerDTO) {
        log.info("requestOK 넘어오는 배열 : " + ownerDTO.getSelectedRow());
        log.info("requestOK ok?no?" + ownerDTO.getCheckStatus());
        String checkStatus = ownerDTO.getCheckStatus();
        log.info("checkStatus : " + checkStatus);
        try{
            if (checkStatus.equals("ok")) {
                log.info("ok 들어옴");
                for (String selected : ownerDTO.getSelectedRow()) {
                    masterService.requestOK(selected);
                }
            } else if (checkStatus.equals("no")) {
                log.info("no 들어옴");
                for (String selected : ownerDTO.getSelectedRow()) {
                   masterService.requestNO(selected);
                }
            }

            return ResponseEntity.ok().body(true);
        }catch (Exception e){
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
            final String id = master.getM_id() + "&MASTER";
            final String token = tokenProvider.create(id);
            log.info(token);
            log.info("master login success");
            List<String> list = new ArrayList<>();
            list.add("ROLE_MASTER");
            final MasterDTO responseDTO = masterDTO.builder()
                    .token(token)
                    .roles(list)
                    .build();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            headers.add("X-AUTH-TOKEN", token);

            return new ResponseEntity<>(responseDTO, headers, HttpStatus.OK);

        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("masterLoing Failed")
                    .build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 전체 회원 리스트 출력
    @GetMapping("userList")
    public ResponseEntity<?> getUserList() {

        try {
            List<UserBean> userList = masterService.userAll();
            log.info("회원 전체 리스트 불러오기");
            return ResponseEntity.ok().body(userList);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }


}
