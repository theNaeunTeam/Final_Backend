package com.douzone.final_backend.Controller;


import com.douzone.final_backend.Common.ResponseDTO;
import com.douzone.final_backend.DTO.ShopListDTO;
import com.douzone.final_backend.DTO.ShoppingCartDTO;
import com.douzone.final_backend.Service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private CommonService commonService;

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

}
