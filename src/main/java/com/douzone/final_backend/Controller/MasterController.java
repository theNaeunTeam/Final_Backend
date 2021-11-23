package com.douzone.final_backend.Controller;

import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.Bean.UserBean;
import com.douzone.final_backend.Common.ResponseDTO;
import com.douzone.final_backend.Common.S3Service;
import com.douzone.final_backend.DTO.BannerDTO;
import com.douzone.final_backend.DTO.OwnerDTO;
import com.douzone.final_backend.Service.MasterService;
import com.douzone.final_backend.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
            log.info("가게 승인중 리스트 불러오기 성공: "+requestList);
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
            log.info("가게 승인중 리스트 불러오기 성공: "+requestList);
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
            log.info("가게 승인중 리스트 불러오기 성공: "+requestList);
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
            log.info("가게 해지완료 리스트 불러오기 성공: "+requestList);
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







    @PostMapping("/banner")
    public ResponseEntity<?> insertBanner(BannerDTO bannerDTO, MultipartFile file) throws IOException {
        log.info("file 정보 :" + file);
        log.info("배너디티오 LIST : " + bannerDTO);

//        if (!files.isEmpty()) {
//            files.forEach((multipartFile) -> {
//                try {
//                    String image = s3Service.upload(multipartFile);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            });
//        }

        return null;

    }



}
