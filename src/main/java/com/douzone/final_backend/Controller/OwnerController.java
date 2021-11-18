package com.douzone.final_backend.Controller;

import com.douzone.final_backend.Bean.GoodsBean;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.Bean.ReserveBean;
import com.douzone.final_backend.Common.ResponseDTO;
import com.douzone.final_backend.Common.S3Service;
import com.douzone.final_backend.DTO.GoodsDTO;
import com.douzone.final_backend.DTO.OwnerDTO;
import com.douzone.final_backend.DTO.ReserveDTO;
import com.douzone.final_backend.Service.OwnerService;
import com.douzone.final_backend.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/owner")
public class OwnerController {

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private S3Service s3Service;

    @GetMapping("/tokencheck")
    public String UserTokenCheck() {
        return "success";
    }

    // 입점 신청
    @PostMapping("/request")
    public ResponseEntity<?> ownerRequest(OwnerDTO ownerDTO, MultipartFile file) {
        try {
            String image = s3Service.upload(file);
            ownerDTO.setO_image(image);

            String encodePW = passwordEncoder.encode(ownerDTO.getO_pw());
            OwnerBean owner = OwnerBean.builder()
                    .o_sNumber(ownerDTO.getO_sNumber())
                    .o_pw(encodePW)
                    .o_address(ownerDTO.getO_address())
                    .o_phone(ownerDTO.getO_phone())
                    .o_cellPhone(ownerDTO.getO_cellPhone())
                    .o_image(ownerDTO.getO_image())
                    .o_name(ownerDTO.getO_name())
                    .o_time1(ownerDTO.getO_time1())
                    .o_time2(ownerDTO.getO_time2())
                    .o_latitude(ownerDTO.getO_latitude())
                    .o_longitude(ownerDTO.getO_longitude())
                    .build();
            ownerService.create(owner);

            log.info("owner 입점 신청 성공");
            return ResponseEntity.ok().body(true);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();

            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }


    // 가게 로그인
    @PostMapping("/ownerlogin")
    public ResponseEntity<?> ownerlogin(@RequestBody OwnerDTO ownerDTO) {
        log.info("들어온 정보 : "+ownerDTO);
        OwnerBean owner = ownerService.getByCredentials(
                ownerDTO.getO_sNumber(),
                ownerDTO.getO_pw(),
                passwordEncoder
        );
        log.info(ownerDTO.getO_pw());

        if (owner != null) {
            final String id = owner.getO_sNumber() + "&" + "OWNER";
            final String token = tokenProvider.create(id);
            log.info(token);

            final OwnerDTO responseOwnerDTO = OwnerDTO.builder()
                    .o_sNumber(owner.getO_sNumber())
                    .o_approval(owner.getO_approval())
                    .o_pw(owner.getO_pw())
                    .token(token)
                    .build();
            log.info("로그인 성공");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            headers.add("X-AUTH-TOKEN", token);
            return new ResponseEntity<>(responseOwnerDTO, headers, HttpStatus.OK);
//            return ResponseEntity.ok().body(responseOwnerDTO);
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login Failed")
                    .build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }


    }

    @PostMapping("/addGoods")
    public ResponseEntity<?> addGoods(GoodsDTO goodsDTO, MultipartFile file) throws IOException {
        log.info("file 정보 :" + file);
        log.info("goods" + goodsDTO);
        String image = s3Service.upload(file);
        goodsDTO.setG_image(image);

        log.info("goodsDTO" + goodsDTO);
        try {
            GoodsBean goods = GoodsBean.builder()
                    .g_owner(goodsDTO.getG_owner())
//                    .g_owner("123")
                    .g_name(goodsDTO.getG_name())
                    .g_count(Integer.parseInt(goodsDTO.getG_count()))
                    .g_price(Integer.parseInt(goodsDTO.getG_price()))
                    .g_discount(Integer.parseInt(goodsDTO.getG_discount()))
                    .g_detail(goodsDTO.getG_detail())
                    .g_image(goodsDTO.getG_image())
                    .g_expireDate(goodsDTO.getG_expireDate())
                    .g_category(goodsDTO.getG_category())
                    .build();
            GoodsBean registerGoods = null;
            if (goodsDTO.getActionType().equals("new")) {
                registerGoods = ownerService.addGoods(goods);
                log.info("상품 등록 완료" + registerGoods);
            } else if (goodsDTO.getActionType().equals("update")) {
                goods.setG_code(Integer.parseInt(goodsDTO.getG_code()));
                registerGoods = ownerService.updateGoods(goods);
                log.info("상품 업데이트 완료" + registerGoods);
            }

            GoodsDTO responseGoodsDTO = GoodsDTO.builder()
                    .g_owner(registerGoods.getG_owner())
//                    .g_owner("123")
                    .g_name(registerGoods.getG_name())
                    .g_count(String.valueOf(registerGoods.getG_count()))
                    .g_price(String.valueOf(registerGoods.getG_price()))
                    .g_discount(String.valueOf(registerGoods.getG_discount()))
                    .g_detail(registerGoods.getG_detail())
                    .g_image(registerGoods.getG_image())
                    .g_expireDate(registerGoods.getG_expireDate())
                    .g_category(registerGoods.getG_category())
                    .build();

            return ResponseEntity.ok().body(responseGoodsDTO);
        } catch (Exception e) {
            log.error("여기서 에러다");
//            log.error(e.getMessage());
            e.printStackTrace();
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 해당 가게 상품 리스트 -> 상품조회
    @GetMapping("goodsView")
    public ResponseEntity<?> goodsView(@RequestParam String o_sNumber) {
        try {
            List<GoodsBean> goodsList = ownerService.goodsList(o_sNumber);

            log.info("사업자 번호" + o_sNumber);

            return ResponseEntity.ok().body(goodsList);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }
    @GetMapping("reserveList")
    public ResponseEntity<?> reservationView(@RequestParam String g_owner){
        log.info("g : "+g_owner);
        try{
            List<ReserveBean> reserveBeans = ownerService.reserveList(g_owner);

            log.info("reserveBeans : "+reserveBeans);


                log.info("if null");
                List<ReserveDTO> responseDTOList = new ArrayList<>();
                for (ReserveBean r : reserveBeans) {
                    log.info("반복문");
                    GoodsBean goods = ownerService.goodsData(r.getR_g_code());

                    ReserveDTO responseDTO = ReserveDTO.builder()
                            .r_code(r.getR_code())
                            .r_u_id(r.getR_u_id())
                            .r_count(r.getR_count())
                            .r_g_code(r.getR_g_code())
                            .r_firstTime(r.getR_firstTime())
                            .r_status(r.getR_status())
                            .r_customOrder(r.getR_customOrder())
                            .r_g_code(r.getR_g_code())
                            .g_name(goods.getG_name())
                            .g_price(goods.getG_price())
                            .g_discount(goods.getG_discount())
                            .g_expireDate(goods.getG_expireDate())
                            .g_category(goods.getG_category())
                            .g_status(goods.getG_status())
                            .g_count(goods.getG_count())
                            .build();
                    log.info("responseDTO : "+responseDTO);

                    responseDTOList.add(responseDTO);

                }
                log.info("responseDTOList : "+responseDTOList);

                return ResponseEntity.ok().body(responseDTOList);

        }catch (Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 예약 상태 변화. 예약 승인완료, 거절, 노쇼
    // r_code 랑 status=승인완료, 거절, 판매완료, 노쇼 정보
    @PostMapping("reserveCheck")
    public ResponseEntity<?> reserveCheck(@RequestBody ReserveDTO reserve) {
        log.info("reserve 넘어온 값 : " + reserve);
        try {
            ownerService.reserveCheck(reserve);
            return ResponseEntity.ok().body(true);


        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("reserveCheck Error");
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }


    }

    // 상품 삭제 시 PatchMapping
    @PatchMapping("/deleteGoods")
    public ResponseEntity<?> deleteGoods(@RequestBody GoodsDTO goodsDTO){
        log.info("deleteGoods 넘어온 값 : "+goodsDTO.getG_code());
        try {
            ownerService.deleteGoods(goodsDTO);
            return ResponseEntity.ok().body(true);
        }catch (Exception e){
            log.error(e.getMessage());
            log.error("deleteGoods Error");

            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

}
