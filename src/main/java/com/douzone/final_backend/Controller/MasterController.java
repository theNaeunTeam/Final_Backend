package com.douzone.final_backend.controller;

import com.douzone.final_backend.vo.OwnerVO;
import com.douzone.final_backend.vo.UserVO;
import com.douzone.final_backend.DTO.BannerDTO;
import com.douzone.final_backend.DTO.LocalDTO;
import com.douzone.final_backend.DTO.OwnerDTO;
import com.douzone.final_backend.DTO.SaleDTO;
import com.douzone.final_backend.DTO.ResponseDTO;
import com.douzone.final_backend.config.S3Service;
import com.douzone.final_backend.config.security.TokenProvider;
import com.douzone.final_backend.service.MasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


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
            List<OwnerVO> requestList = masterService.findAll();
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
            List<UserVO> userList = masterService.userAll();
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
            List<OwnerVO> requestList = masterService.findApproval();
            log.info("가게 승인중 리스트 불러오기 성공: " + requestList);
            return ResponseEntity.ok().body(requestList);

        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 가게 승인완료 리스트 불러오기
    @GetMapping("/approvalCompletion")
    public ResponseEntity<?> approvalWaiting() {

        try {
            List<OwnerVO> requestList = masterService.approvalCompletion();
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
            List<OwnerVO> requestList = masterService.terminationWaiting();
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
            List<OwnerVO> requestList = masterService.terminationCompletion();
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

    // 월별 오너 가입 탈퇴수 가져오기
    @GetMapping("/masterMonth")
    public ResponseEntity MonthMasterChart() {
        log.info("masterMonth 들어왓다");
        try {
            int nowYear = Calendar.getInstance().get(Calendar.YEAR);
            log.info("" + nowYear);
            List<Object> responseMonList = new ArrayList<>();
            for (int dal = 2019; dal <= nowYear; dal++) {
                List<SaleDTO> mon = masterService.masterMonth(dal);
                responseMonList.add(mon);
            }
            List<SaleDTO> year = masterService.masterYear(nowYear);
            log.info("000000000000000000000000");
            log.info("" + year);
            SaleDTO result = SaleDTO.builder()
                    .totalMon(responseMonList)
                    .year(year)
                    .build();

            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 월별 유저 가입 탈퇴수 가져오기
    @GetMapping("/userMonth")
    public ResponseEntity MonthUserChart() {
        log.info("UserMonth 들어왓다");
        try {
            int nowYear = Calendar.getInstance().get(Calendar.YEAR);
            log.info("" + nowYear);
            List<Object> responseMonList = new ArrayList<>();
            for (int dal = 2019; dal <= nowYear; dal++) {
                List<SaleDTO> mon = masterService.userMonth(dal);
                responseMonList.add(mon);
            }
            List<SaleDTO> year = masterService.userYear(nowYear);

            SaleDTO result = SaleDTO.builder()
                    .totalMon(responseMonList)
                    .year(year)
                    .build();

            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 월별 오너 + 유저 가입자수 가져오기 - MasterChart.tsx
    @GetMapping("/OwnerUserChart")
    public ResponseEntity OnwerUserChart() {
        log.info("MasterUserChart 들어왓다");
        try {
            int nowYear = Calendar.getInstance().get(Calendar.YEAR);
            log.info("" + nowYear);
            List<Object> responseMonList = new ArrayList<>();
            for (int dal = 2019; dal <= nowYear; dal++) {
                List<SaleDTO> mon = masterService.ownerUser(dal);
                responseMonList.add(mon);
            }
            List<SaleDTO> year = masterService.onnerUserYear(nowYear);

            SaleDTO result = SaleDTO.builder()
                    .totalMon(responseMonList)
                    .year(year)
                    .build();

            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 월별 오너 + 유저 탈퇴수 가져오기 - MasterChart2.tsx
    @GetMapping("/OwnerUserChart2")
    public ResponseEntity OnwerUserChart2() {
        log.info("MasterUserChart 들어왓다");
        try {
            int nowYear = Calendar.getInstance().get(Calendar.YEAR);
            log.info("" + nowYear);
            List<Object> responseMonList = new ArrayList<>();
            for (int dal = 2019; dal <= nowYear; dal++) {
                List<SaleDTO> mon = masterService.ownerUser2(dal);
                responseMonList.add(mon);
            }
            List<SaleDTO> year = masterService.onnerUserYear2(nowYear);

            SaleDTO result = SaleDTO.builder()
                    .totalMon(responseMonList)
                    .year(year)
                    .build();

            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 지역
    @GetMapping("/OwnerUserChart3")
    public ResponseEntity OnwerUserChart3() {
        log.info("OwnerUserChart3 들어왓다");
        try {
            LocalDTO localDTO = masterService.OwnerUserChart3();

            return ResponseEntity.ok().body(localDTO);
        } catch (Exception e) {
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
