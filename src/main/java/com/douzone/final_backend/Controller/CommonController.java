package com.douzone.final_backend.Controller;


import com.douzone.final_backend.Bean.GoodsBean;
import com.douzone.final_backend.Bean.MasterBean;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.Bean.UserBean;
import com.douzone.final_backend.Common.ResponseDTO;
import com.douzone.final_backend.Common.S3Service;
import com.douzone.final_backend.DTO.*;
import com.douzone.final_backend.Service.CommonService;
import com.douzone.final_backend.Service.MasterService;
import com.douzone.final_backend.Service.OwnerService;
import com.douzone.final_backend.Service.UserService;
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
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private UserService userService;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private MasterService masterService;
    @Autowired
    private S3Service s3Service;

    @Autowired
    private TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //위치 근처 가게 리스트
    @GetMapping("/list")
    public ResponseEntity<?> shopList(@RequestParam String LAT, String LON, String RAD) {
        ShopListDTO s = ShopListDTO.builder()
                .o_longitude(LON)
                .o_latitude(LAT)
                .radius(RAD)
                .build();
        try {
            List<ShopListDTO> list = commonService.getShopList(s);
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 장바구니
    @PostMapping("/shoppingcart")
    public ResponseEntity<?> shoppingCartDTOS(@RequestBody List<String> lists) {
        List<ShoppingCartDTO> newList = new ArrayList<>();

        lists.forEach(a -> {
            newList.add(commonService.getShoppingCart(a));
        });

        try {
            return ResponseEntity.ok().body(newList);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 유저 회원가입
    @PostMapping("/userjoin")
    public ResponseEntity<?> userjoin(@RequestBody UserDTO userDTO) {
        log.info("회원가입 들어옴" + userDTO);
        try {
            String encodePW = passwordEncoder.encode(userDTO.getU_pw());
            UserBean user = UserBean.builder()
                    .u_id(userDTO.getU_id())
                    .u_pw(encodePW)
                    .u_cellPhone(userDTO.getU_cellPhone())
                    .u_email(userDTO.getU_email())
                    .u_gender(userDTO.getU_gender())
                    .u_age(userDTO.getU_age())
                    .build();
            UserBean registerUser = userService.create(user);
            UserDTO responseUserDTO = UserDTO.builder()
                    .u_id(registerUser.getU_id())
                    .u_pw(registerUser.getU_pw())
                    .u_cellPhone(registerUser.getU_cellPhone())
                    .u_email(registerUser.getU_email())
                    .u_gender(registerUser.getU_gender())
                    .u_age(registerUser.getU_age())
                    .build();
            log.info("회원가입 성공");
            return ResponseEntity.ok().body(responseUserDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();

            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }

    }

    // 유저 로그인
    @PostMapping("/userlogin")
    public ResponseEntity<?> userlogin(@RequestBody UserDTO userDTO) {
        UserBean user = userService.getByCredentials(
                userDTO.getU_id(),
                userDTO.getU_pw(),
                passwordEncoder
        );
        log.info("user : " + userDTO.getU_pw() + user);

        if (user != null) {
            final String id = user.getU_id() + "&USER";
            log.info(id);
            final String token = tokenProvider.create(id);

            final UserDTO responseUserDTO = UserDTO.builder()
//                    .u_id(user.getU_id())
//                    .u_pw(user.getU_pw())
//                    .u_cellPhone(user.getU_cellPhone())
//                    .u_email(user.getU_email())
//                    .u_gender(user.getU_gender())
//                    .u_age(user.getU_age())
//                    .roles(list)
                    .token(token)
                    .build();
            log.info(token);
            log.info("로그인 성공");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            headers.add("X-AUTH-TOKEN", token);
            return new ResponseEntity<>(responseUserDTO, headers, HttpStatus.OK);
//            return ResponseEntity.ok().body(responseUserDTO);
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login Failed")
                    .build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }


    }

    // 가게 상세 페이지
    @GetMapping("/storeGoodsView")
    public ResponseEntity<?> shopView(@RequestParam String o_sNumber) {
        log.info(o_sNumber);
        try {
            List<GoodsBean> goodsList = userService.storeGoodsView(o_sNumber);
            log.info("shopView" + goodsList);
            return ResponseEntity.ok().body(goodsList);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 해당 가게 정보 상세보기
    @GetMapping("/storeView")
    public ResponseEntity<?> storeView(@RequestParam String o_sNumber) {
        log.info("storeView 들어오는 값 : " + o_sNumber);
        try {
            OwnerBean ownerBean = userService.findByStore(o_sNumber);
            OwnerDTO responseDTO = OwnerDTO.builder()
                    .o_sNumber(ownerBean.getO_sNumber())
                    .o_address(ownerBean.getO_address())
                    .o_image(ownerBean.getO_image())
                    .o_cellPhone(ownerBean.getO_cellPhone())
                    .o_name(ownerBean.getO_name())
                    .o_phone(ownerBean.getO_phone())
                    .o_time1(ownerBean.getO_time1())
                    .o_time2(ownerBean.getO_time2())
                    .o_latitude(ownerBean.getO_latitude())
                    .o_longitude(ownerBean.getO_longitude())
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
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
        log.info("들어온 정보 : " + ownerDTO);
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

    @GetMapping("/getCategory")
    public ResponseEntity<?> getCategory(@RequestParam String g_owner) {
        try {
            HashMap<String, Object> list = commonService.getCategory(g_owner);
            list.put("g_owner", g_owner);
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @GetMapping("/banner")
    public ResponseEntity<?> getBanner() throws IOException {
        try {
            List<BannerDTO> list = masterService.getBanner();
            return ResponseEntity.ok().body(list);

        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @GetMapping("/recommendList")
    public ResponseEntity<?> recommendList() {
        try {
            List<RecommendListDTO> list = commonService.getRecommendList();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
