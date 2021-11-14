package com.douzone.final_backend.Owner;

import com.douzone.final_backend.Common.ResponseDTO;
import com.douzone.final_backend.Common.S3Service;
import com.douzone.final_backend.Goods.GoodsBean;
import com.douzone.final_backend.Goods.GoodsDTO;
import com.douzone.final_backend.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    // 입점 신청
    @PostMapping("/request")
    public ResponseEntity<?> ownerRequest(@RequestBody OwnerDTO ownerDTO) {
        try {
            String encodePW = passwordEncoder.encode(ownerDTO.getO_pw());
            OwnerBean owner = OwnerBean.builder()
                    .o_sNumber(ownerDTO.getO_sNumber())
                    .o_pw(encodePW)
                    .o_address(ownerDTO.getO_address())
                    .o_phone(ownerDTO.getO_phone())
                    .o_cellPhone(ownerDTO.getO_cellPhone())
                    .o_image(ownerDTO.getO_image())
                    .o_name(ownerDTO.getO_name())
                    .o_time(ownerDTO.getO_time1() + "-" + ownerDTO.getO_time2())
                    .build();
            OwnerBean registerOwner = ownerService.create(owner);
            OwnerDTO responseOwnerDTO = OwnerDTO.builder()
                    .o_sNumber(registerOwner.getO_sNumber())
                    .o_approval(registerOwner.getO_approval())
                    .o_pw(registerOwner.getO_pw())
                    .build();
            log.info("owenr 입점 신청 성공");
            return ResponseEntity.ok().body(responseOwnerDTO);
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
            List<String> list = new ArrayList<>();
            list.add("ROLE_OWNER");
            final OwnerDTO responseOwnerDTO = OwnerDTO.builder()
                    .o_sNumber(owner.getO_sNumber())
                    .o_approval(owner.getO_approval())
                    .o_pw(owner.getO_pw())
                    .roles(list)
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
    public ResponseEntity<?> addGoods( GoodsDTO goodsDTO, MultipartFile file) throws IOException {
       log.info("file 정보 :" + file);
       log.info("goods" + goodsDTO);
        String image = s3Service.upload(file);
        goodsDTO.setG_image(image);

        log.info("goodsDTO" + goodsDTO);
        try {

            GoodsBean goods = GoodsBean.builder()
//                    .g_owner(goodsDTO.getG_owner())
                    .g_owner("123")
                    .g_name(goodsDTO.getG_name())
                    .g_count(goodsDTO.getG_count())
                    .g_price(goodsDTO.getG_price())
                    .g_discount(goodsDTO.getG_discount())
                    .g_detail(goodsDTO.getG_detail())
                    .g_image(goodsDTO.getG_image())
                    .g_expireDate(goodsDTO.getG_expireDate())
                    .g_category(goodsDTO.getG_category())
                    .build();
            GoodsBean registerGoods = ownerService.addGoods(goods);
            GoodsDTO responseGoodsDTO = GoodsDTO.builder()
//                    .g_owner(registerGoods.getG_owner())
                    .g_owner("123")
                    .g_name(registerGoods.getG_name())
                    .g_count(registerGoods.getG_count())
                    .g_price(registerGoods.getG_price())
                    .g_discount(registerGoods.getG_discount())
                    .g_detail(registerGoods.getG_detail())
                    .g_image(registerGoods.getG_image())
                    .g_expireDate(registerGoods.getG_expireDate())
                    .g_category(registerGoods.getG_category())
                    .build();

            log.info("상품 등록 완료" + registerGoods);
            return ResponseEntity.ok().body(responseGoodsDTO);
        } catch (Exception e) {
            log.error("여기서 에러다");
            log.error(e.getMessage());
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

}
