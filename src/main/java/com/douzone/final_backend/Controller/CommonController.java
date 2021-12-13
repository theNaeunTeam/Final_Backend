package com.douzone.final_backend.Controller;


import com.douzone.final_backend.Bean.GoodsBean;
import com.douzone.final_backend.Bean.MasterBean;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.Bean.UserBean;
import com.douzone.final_backend.Common.MailService;
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
    @Autowired
    MailService mailService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //위치 근처 가게 리스트
    @GetMapping("/list")
    public ResponseEntity<?> shopList(@RequestParam String LAT, String LON, String RAD, int startIndex, String goodsName, String sortOption) {
        ShopListDTO s = ShopListDTO.builder()
                .o_longitude(LON)
                .o_latitude(LAT)
                .radius(RAD)
                .startIndex(startIndex)
                .goodsName(goodsName)
                .sortOption(sortOption)
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
    // 현재 위치 저장 되어있을 경우 근처 가게 4개만 뽑아서 내보내기
    @GetMapping("/localList")
    public ResponseEntity<?> localList(@RequestParam String LAT, String LON){
        String range = "1.5";
        log.info("localList 들어옴");
        ShopListDTO s = ShopListDTO.builder()
                .o_latitude(LAT)
                .o_longitude(LON)
                .radius(range)
                .build();
        try{
            List<ShopListDTO> list = commonService.getLocalList(s);
            return ResponseEntity.ok().body(list);
        }catch (Exception e){
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
        log.info("login");

        // 아이디가 존재하지 않거나 비밀번호가 틀리면 catch 문으로 간다.
        // service 에서 예외처리 해줌.
        try {
            log.info("login1" + userDTO.getU_id());
            UserBean user = userService.getByCredentials(
                    userDTO.getU_id(),
                    userDTO.getU_pw(),
                    passwordEncoder
            );
            log.info("user : " + userDTO.getU_pw() + user);

            final String id = user.getU_id();
            log.info(id);
            final String token = tokenProvider.create(id);

            final UserDTO responseUserDTO = UserDTO.builder()
                    .token(token)
                    .build();
            log.info(token);
            log.info("로그인 성공");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            headers.add("X-AUTH-TOKEN", token);
            return new ResponseEntity<>(responseUserDTO, headers, HttpStatus.OK);

        } catch (Exception e) {
            log.info(e.getMessage());
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
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
            int result = ownerService.create(owner);

            log.info("owner 입점 신청 성공");
            return ResponseEntity.ok().body(result);
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

        try {
            OwnerBean owner = ownerService.getByCredentials(
                    ownerDTO.getO_sNumber(),
                    ownerDTO.getO_pw(),
                    passwordEncoder
            );
            log.info(ownerDTO.getO_pw());

            final String id = owner.getO_sNumber();
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
//            return ResponseEntity.ok().body(responseOwnerDTO);}


        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    // 마스터 로그인 true, false
    @PostMapping("masterlogin")
    public ResponseEntity<?> masterLogin(@RequestBody MasterDTO masterDTO) {
        log.info("masterDTO : " + masterDTO);
        try {
            MasterBean master = masterService.login(masterDTO);
//        MasterDTO master = masterService.findMaster(masterDTO);
            log.info("master ??" + master);

            final String id = master.getM_id();
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
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
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



    @PostMapping("/userFindPW")
    public ResponseEntity<?> userFindPW(@RequestBody UserDTO userDTO) {
        log.info("id : "+userDTO.getU_id());

        // id로 유저 정보 가져오기
        try{
            UserBean user = userService.findById(userDTO.getU_id());
            String u_pw = user.getU_pw().substring(0, 20);
            log.info("u_pw : "+u_pw);

            String resPW = u_pw.replace("/","가");
            log.info("resPW : "+resPW);

            mailService.sendMail(user.getU_email(), user.getU_id(), resPW);
            log.info("sendMail");
            return ResponseEntity.ok().body(true);
        }catch (Exception e){
            log.info(e.getMessage());
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    // 링크 파라미터로 전해온 아이디와 비밀번호가 유효한지 검사
    @PostMapping("changePWcheck")
    public ResponseEntity<?> changePWcheck(@RequestBody UserDTO userDTO){
        log.info("들어온 값 : "+userDTO);
        UserDTO responseDTO = userService.changePWcheck(userDTO);
        log.info("결과 : "+responseDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("changePW")
    public ResponseEntity<?> changePW(@RequestBody UserDTO userDTO){
        // 해당 비밀번호를 암호화해서 update
        // 비밀번호 암호화해서 전송
        try{
            String encodePW = passwordEncoder.encode(userDTO.getU_pw());
            UserBean user = UserBean.builder()
                    .u_id(userDTO.getU_id())
                    .u_pw(encodePW)
                    .build();
            int result = userService.pwUpdate(user);

            return ResponseEntity.ok().body(result);
        }catch(Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


}
