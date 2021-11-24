package com.douzone.final_backend.Controller;

import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.Bean.UserBean;
import com.douzone.final_backend.Common.ResponseDTO;
import com.douzone.final_backend.Common.S3Service;
import com.douzone.final_backend.DTO.BannerDTO;
import com.douzone.final_backend.DTO.OwnerDTO;
import com.douzone.final_backend.DTO.ReserveDTO;
import com.douzone.final_backend.DTO.SaleDTO;
import com.douzone.final_backend.Service.MasterService;
import com.douzone.final_backend.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/master")
public class MasterController {
    @Autowired
    private MasterService masterService;
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private S3Service s3Service;

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
        try {
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
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }

    }


    // 전체 회원 리스트 출력
    @GetMapping("/userList")
    public ResponseEntity<?> getUserList() {

        try {
            List<UserBean> userList = masterService.userAll();
            log.info("회원 전체 리스트 불러오기" + userList);
            return ResponseEntity.ok().body(userList);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }


    // 가게 승인대기중 리스트 불러오기
    @GetMapping("/approvalWaiting")
    public ResponseEntity<?> getApprovalWaiting() {

        try {
            List<OwnerBean> requestList = masterService.findApproval();
            log.info("가게 승인중 리스트 불러오기 성공: " + requestList);
            return ResponseEntity.ok().body(requestList);

        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 가게 승인대기중 리스트 불러오기
    @GetMapping("/approvalCompletion")
    public ResponseEntity<?> approvalWaiting() {

        try {
            List<OwnerBean> requestList = masterService.approvalCompletion();
            log.info("가게 승인중 리스트 불러오기 성공: " + requestList);
            return ResponseEntity.ok().body(requestList);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 가게 해지대기중 리스트 불러오기
    @GetMapping("/terminationwaiting")
    public ResponseEntity<?> terminationWaiting() {

        try {
            List<OwnerBean> requestList = masterService.terminationWaiting();
            log.info("가게 승인중 리스트 불러오기 성공: " + requestList);
            return ResponseEntity.ok().body(requestList);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 가게 해지승인 처리
    @PatchMapping("/terminationOK")
    public ResponseEntity<?> terminationOK(@RequestBody OwnerDTO ownerDTO) {
        log.info("terminationOK 넘어오는 배열 : " + ownerDTO.getSelectedRow());
        log.info("terminationOK ok?no?" + ownerDTO.getCheckStatus());
        String checkStatus = ownerDTO.getCheckStatus();
        log.info("checkStatus : " + checkStatus);
        try {
            if (checkStatus.equals("ok")) {
                log.info("ok 들어옴");
                for (String selected : ownerDTO.getSelectedRow()) {
                    masterService.terminationOK(selected);
                }
            }
            return ResponseEntity.ok().body(true);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 가게 해지완료 리스트 불러오기
    @GetMapping("/terminationcompletion")
    public ResponseEntity<?> terminationCompletion() {
        try {
            List<OwnerBean> requestList = masterService.terminationCompletion();
            log.info("가게 해지완료 리스트 불러오기 성공: " + requestList);
            return ResponseEntity.ok().body(requestList);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }


    // 가게 해지반려 처리
    @PatchMapping("/terminationcancle")
    public ResponseEntity<?> terminationCancle(@RequestBody OwnerDTO ownerDTO) {
        log.info("terminationCancle 넘어오는 배열 : " + ownerDTO.getSelectedRow());
        log.info("terminationCancle ok?no?" + ownerDTO.getCheckStatus());
        String checkStatus = ownerDTO.getCheckStatus();
        log.info("checkStatus : " + checkStatus);
        try {
            if (checkStatus.equals("ok")) {
                log.info("ok 들어옴");
                for (String selected : ownerDTO.getSelectedRow()) {
                    masterService.terminationCancle(selected);
                }
            }
            return ResponseEntity.ok().body(true);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }
  
    // 월별 가입자수 가져오기
    @GetMapping("/masterMonth")
    public ResponseEntity MonthChart(){
        log.info("masterMonth 들어왓다");
        try{
            int nowYear = Calendar.getInstance().get(Calendar.YEAR);
            log.info(""+nowYear);
            List<Object> responseMonList = new ArrayList<>();
            List<Object> responseYearList = new ArrayList<>();
            for (int dal= 2019 ; dal <= nowYear ; dal++){
                List<SaleDTO> mon =  masterService.masterMonth(dal);
                List<SaleDTO> year = masterService.masterYear(dal);
                responseMonList.add(mon);
                responseYearList.add(year);
            }
            SaleDTO result = SaleDTO.builder()
                    .totalMon(responseMonList)
                    .totalYear(responseYearList)
                    .build();
            return ResponseEntity.ok().body(result);

        }catch (Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }


    @PostMapping("/bannerImage")
    public HashMap<String, Object> InsertBannerImage(MultipartFile file) {
        HashMap<String, Object> hashMap = new HashMap<>();
        log.info("files 정보 :" + file);
        try {
            String image = s3Service.upload(file);
            hashMap.put("res", image);
        } catch (IOException e) {
            hashMap.put("res", e);
        }
        return hashMap;
    }

    @PutMapping("/bannerContents")
    public HashMap<String, Object> UpdateBannerContents(@RequestBody List<BannerDTO> list) {
        HashMap<String, Object> hashMap = new HashMap<>();
        log.info("받은 배열 정보 :" + list);

        try {
            masterService.deleteBannerTable();
            list.forEach(bannerDTO -> {
                masterService.insertBannerTable(bannerDTO);
            });

            hashMap.put("res", list);
        } catch (Exception e) {
            hashMap.put("res", e);
        }

        return hashMap;
    }

}
