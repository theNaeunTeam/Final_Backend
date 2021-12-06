package com.douzone.final_backend.Controller;

import com.douzone.final_backend.Bean.GoodsBean;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.Bean.ReserveBean;
import com.douzone.final_backend.Common.ResponseDTO;
import com.douzone.final_backend.Common.S3Service;
import com.douzone.final_backend.DTO.*;
import com.douzone.final_backend.Service.OwnerService;
import com.douzone.final_backend.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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

    // owner Main page
    // 가게 기본 정보
    @GetMapping
    public ResponseEntity<?> main(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("여기 들어옴" + userDetails.getUsername());
        String o_sNumber = userDetails.getUsername();
        OwnerPageDTO owner = ownerService.getOwner(o_sNumber);
        log.info("ownerPage : " + owner);
        return ResponseEntity.ok().body(owner);
    }
    // owner  main 들어왔을 때 보이는 차트 정보
    // 연도별, 월별, 일별 매출
    @GetMapping("getDay")
    public ResponseEntity<?> getDay(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("getDay 들어옴");
        String o_sNumber = userDetails.getUsername();

        List<Object> mon = new ArrayList<>();
        List<Object> day = new ArrayList<>();
        int y = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = 2019; i <= y; i++) {
            OwnerPageDTO mm = OwnerPageDTO.builder()
                    .o_name(o_sNumber)
                    .total(i)
                    .build();
            List<SaleDTO> m = ownerService.getMon(mm);
            mon.add(m);
            int a = 12;
            // 올해의 현재 월까지만
            if (i == y) {
                a = Calendar.getInstance().get(Calendar.MONTH) + 1;
                log.info(a + " a");
            }
            for (int j = 1; j <= a; j++) {
                OwnerPageDTO dd = OwnerPageDTO.builder()
                        .o_name(o_sNumber)
                        .total(i)
                        .monTotal(j)
                        .build();
                List<SaleDTO> d = ownerService.getDay(dd);
                day.add(d);
            }
//            log.info("만든 d"+day);
        }

        List<SaleDTO> year = ownerService.getYear(o_sNumber);
        SaleDTO responseDTO = SaleDTO.builder()
                .d(day)
//                .mon(mon)
                .m(mon)
                .year(year)
                .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/addGoods")
    public ResponseEntity<?> addGoods(@AuthenticationPrincipal UserDetails userDetails, GoodsDTO goodsDTO, MultipartFile file) throws IOException {
        log.info("file 정보 :" + file);
        log.info("goodsDTO" + goodsDTO);
        try {
            if(file == null){
                throw new RuntimeException("이미지를 선택해주세요");
            }

            String image = s3Service.upload(file);
            goodsDTO.setG_image(image);
            goodsDTO.setG_owner(userDetails.getUsername());

            GoodsBean goods = GoodsBean.builder()
                    .g_owner(goodsDTO.getG_owner())
//                    .g_owner("123")
                    .g_name(goodsDTO.getG_name())
                    .g_count(goodsDTO.getG_count())
                    .g_price(goodsDTO.getG_price())
                    .g_discount(goodsDTO.getG_discount())
                    .g_detail(goodsDTO.getG_detail())
                    .g_image(goodsDTO.getG_image())
                    .g_expireDate(goodsDTO.getG_expireDate())
                    .g_category(goodsDTO.getG_category())
                    .build();
            GoodsBean registerGoods = null;
            int result = 0;
            if (goodsDTO.getActionType().equals("new")) {
//                registerGoods = ownerService.addGoods(goods);
                result =  ownerService.addGoods(goods);
                log.info("상품 등록 완료" + registerGoods);
            } else if (goodsDTO.getActionType().equals("update")) {
                goods.setG_code(goodsDTO.getG_code());
//                registerGoods = ownerService.updateGoods(goods);
                result =  ownerService.updateGoods(goods);
                log.info("상품 업데이트 완료" + registerGoods);
            }

//            GoodsDTO responseGoodsDTO = GoodsDTO.builder()
//                    .g_owner(registerGoods.getG_owner())
////                    .g_owner("123")
//                    .g_name(registerGoods.getG_name())
//                    .g_count(registerGoods.getG_count())
//                    .g_price(registerGoods.getG_price())
//                    .g_discount(registerGoods.getG_discount())
//                    .g_detail(registerGoods.getG_detail())
//                    .g_image(registerGoods.getG_image())
//                    .g_expireDate(registerGoods.getG_expireDate())
//                    .g_category(registerGoods.getG_category())
//                    .build();

            return ResponseEntity.ok().body(result);
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
//    public ResponseEntity<?> goodsView(@RequestParam String o_sNumber) {
    public ResponseEntity<?> goodsView(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("사업자 번호" + userDetails.getUsername());

        try {
            // 해당 가게 상품 리스트
            List<GoodsBean> goodsList = ownerService.goodsList(userDetails.getUsername());
            List<GoodsDTO> responseDTO = new ArrayList<>();
            log.info("try문 ");

            // 해당 상품 예약 판매 완료된 갯수
            for (GoodsBean g : goodsList) {
                int count = ownerService.getGoodsReserve(g.getG_code());

                GoodsDTO dto = GoodsDTO.builder()
                        .g_name(g.getG_name())
                        .g_code(g.getG_code())
                        .g_category(g.getG_category())
                        .g_price(g.getG_price())
                        .g_discount(g.getG_discount())
                        .g_expireDate(g.getG_expireDate())
                        .g_status(g.getG_status())
                        .g_count(g.getG_count())
                        .cnt(count)
                        .build();
                responseDTO.add(dto);
            }

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @GetMapping("reserveList")
    public ResponseEntity<?> reservationView(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String g_owner) {
        log.info("g : " + g_owner);
        log.info("reserveList 주인 :  " + userDetails.getUsername());
        try {
            List<ReserveBean> reserveBeans = ownerService.reserveListAll(g_owner);
            log.info("if null");
            List<ReserveDTO> responseDTOList = new ArrayList<>();
            for (ReserveBean r : reserveBeans) {
                GoodsBean goods = ownerService.goodsData(r.getR_g_code());

//                GoodsDTO goods = GoodsDTO.builder()
//                        .g_name(goodsBean.getG_name())
//                        .g_price(goodsBean.getG_price())
//                        .g_discount(goodsBean.getG_discount())
//                        .g_expireDate(goodsBean.getG_expireDate())
//                        .g_category(goodsBean.getG_category())
//                        .g_status(goodsBean.getG_status())
//                        .g_count(goodsBean.getG_count())
//                        .build();
                ReserveDTO responseDTO = ReserveDTO.builder()
                        .r_code(r.getR_code())
                        .r_u_id(r.getR_u_id())
                        .r_count(r.getR_count())
                        .r_g_code(r.getR_g_code())
                        .r_firstTime(r.getR_firstTime())
                        .r_status(r.getR_status())
                        .r_customOrder(r.getR_customOrder())
                        .r_firstDate(r.getR_firstDate())
//                        .goodsDTO(goods)
                        .g_discount(goods.getG_discount())
                        .g_price(goods.getG_price())
                        .g_name(goods.getG_name())
                        .g_expireDate(goods.getG_expireDate())
                        .g_category(goods.getG_category())
                        .g_status(goods.getG_status())
                        .g_count(goods.getG_count())
                        .r_pay(r.getR_pay())
                        .build();

                responseDTOList.add(responseDTO);

            }
//                log.info("responseDTOList : "+responseDTOList);
            return ResponseEntity.ok().body(responseDTOList);

        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 예약 상태 변화. 예약 승인완료, 거절, 노쇼
    // r_code 랑 status=승인완료, 거절, 판매완료, 노쇼 정보
    @PatchMapping("/statusChange")
    public ResponseEntity<?> reserveCheck(@RequestBody ReserveDTO reserve) {
        log.info("reserve 넘어온 값 : " + reserve);
        try {
            ReserveBean reserveBean = ownerService.reserveOne(reserve);

            ReserveDTO responseDTO = ReserveDTO.builder()
                    .r_count(reserveBean.getR_count())
                    .r_code(reserveBean.getR_code())
                    .r_g_code(reserveBean.getR_g_code())
                    .r_u_id(reserveBean.getR_u_id())
                    .r_status(reserveBean.getR_status())
                    .r_pay(reserveBean.getR_pay())
                    .check(reserve.getCheck())
                    .build();
            log.info("빌드한 responseDTO : " + responseDTO);
            ownerService.reserveCheck(responseDTO);

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
    public ResponseEntity<?> deleteGoods(@RequestBody GoodsDTO goodsDTO) {
        log.info("deleteGoods 넘어온 값 : " + goodsDTO);
        int g_code = goodsDTO.getG_code();
        try {
            int result = ownerService.deleteGoods(g_code);

            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("deleteGoods Error");

            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 예약 현황에서 검색
    @GetMapping("searchReserve")
    public ResponseEntity<?> searchReserve(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(required = false) String g_category, @RequestParam(required = false) String r_status, @RequestParam(required = false) String searchInput,@RequestParam(required = false) int startIndex) {
        log.info("예약현황에서 검색" + g_category + r_status + searchInput);
        // 넘어오는 값 g_category, r_status, searchInput(상품명)
        String o_sNumber = userDetails.getUsername();
        ReserveDTO r;
        if (r_status.equals("")) {
            r = ReserveDTO.builder()
                    .g_category(g_category)
                    .searchInput(searchInput)
                    .r_status(9999)
                    .r_owner(o_sNumber)
                    .startIndex(startIndex)
                    .build();
        } else {
            r = ReserveDTO.builder()
                    .g_category(g_category)
                    .r_status(Integer.parseInt(r_status))
                    .searchInput(searchInput)
                    .r_owner(o_sNumber)
                    .startIndex(startIndex)
                    .build();
        }
        log.info("만들어진 r : " + r);

        log.info("###################");
        List<ReserveDTO> responseDTO = ownerService.searchReserve(r);
        log.info("searchReserve : " + o_sNumber + g_category);
        log.info("responseDTO : " + responseDTO);

        return ResponseEntity.ok().body(responseDTO);

    }

    // 상품 조회에서 검색
    @GetMapping("search")
    public ResponseEntity<?> search(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(required = false) String g_category, @RequestParam(required = false) String g_status, @RequestParam(required = false) String searchInput, @RequestParam(required = false) int startIndex) {
        String g_owner = userDetails.getUsername();
        log.info("search 넘어온 값 : " + userDetails + g_category + g_status + searchInput);

        GoodsDTO g;
        if (g_status.equals("")) {
            g = GoodsDTO.builder()
                    .g_owner(g_owner)
                    .g_category(g_category)
                    .g_status(9999)
                    .searchInput(searchInput)
                    .startIndex(startIndex)
                    .build();
            log.info("status 값 공백");
        } else {
            g = GoodsDTO.builder()
                    .g_owner(g_owner)
                    .g_category(g_category)
                    .g_status(Integer.parseInt(g_status))
                    .searchInput(searchInput)
                    .startIndex(startIndex)
                    .build();
            log.info("stauts 값 : " + g_status);
        }

        log.info("search Build 성공");

        List<GoodsDTO> responseDTO = ownerService.search(g);
        for(GoodsDTO dto : responseDTO){
            int count = ownerService.getGoodsReserve(dto.getG_code());
            dto.setCnt(count);
        }
        log.info("cnt set ?? "+responseDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("pushToken")
    public boolean pushToken(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PushTokenDTO pushTokenDTO) {
        String o_sNumber = userDetails.getUsername();
        log.info("pushToken" + pushTokenDTO);
        pushTokenDTO.setG_owner_fk(o_sNumber);
        return ownerService.pushToken(pushTokenDTO);
    }

    // 오너 대시보드 예약 시간별 보기
    @GetMapping("getTime")
    public ResponseEntity<?> getTime(@AuthenticationPrincipal UserDetails userDetails) {
        String r_owner = userDetails.getUsername();
        try {
            List<SaleDTO> dto = ownerService.getTime(r_owner);

            return ResponseEntity.ok().body(dto);

        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();

            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 오너 대시보드 판매 완료 남여비율
    @GetMapping("getGender")
    public ResponseEntity<?> getGender(@AuthenticationPrincipal UserDetails userDetails) {
        String r_owner = userDetails.getUsername();

        try {
            List<SaleDTO> dto = ownerService.getGender(r_owner);

            return ResponseEntity.ok().body(dto);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();

            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 오너 대시보드 판매 완료 연령대 비율
    @GetMapping("getAge")
    public ResponseEntity<?> getAge(@AuthenticationPrincipal UserDetails userDetails) {
        String r_owner = userDetails.getUsername();

        try {
            List<SaleDTO> dto = ownerService.getAge(r_owner);

            return ResponseEntity.ok().body(dto);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();

            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 오너 대시보드 판매 완료 카테고리
    @GetMapping("getCategorySale")
    public ResponseEntity<?> getCategorySale(@AuthenticationPrincipal UserDetails userDetails) {
        String r_owner = userDetails.getUsername();

        try {
            List<SaleDTO> dto = ownerService.getCategorySale(r_owner);

            return ResponseEntity.ok().body(dto);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 오너 노쇼발생, 취소 발생, 유통기한 경과 판매중지 건수와 확률
    @GetMapping("getOwnerBoard")
    public ResponseEntity<?> getOwnerBoard(@AuthenticationPrincipal UserDetails userDetails) {
        String owner = userDetails.getUsername();
        log.info("getOwnerBoard 들어옴");
        try {
            SaleDTO noshow = ownerService.getNoShow(owner);
            SaleDTO cancel = ownerService.getCancel(owner);
            SaleDTO over = ownerService.getOver(owner);

            SaleDTO responseDTO = SaleDTO.builder()
                    .a(noshow)
                    .b(cancel)
                    .c(over)
                    .build();
            return ResponseEntity.ok().body(responseDTO);


        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 가맹 해지 신청
    @PostMapping("ownerExit")
    public ResponseEntity<?> ownerExit(@AuthenticationPrincipal UserDetails userDetails, @RequestBody OwnerDTO dto) {
        String o_sNumber = userDetails.getUsername();
        log.info("ownerExit " + dto);
        log.info("ownerExit " + o_sNumber);

        try {
            OwnerBean owner = ownerService.getByCredentials(o_sNumber, dto.getO_pw(), passwordEncoder);

            int result = ownerService.ownerExit(o_sNumber);
            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

}


