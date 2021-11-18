package com.douzone.final_backend.Controller;


import com.douzone.final_backend.Common.ResponseDTO;
import com.douzone.final_backend.DTO.ShoppingCartDTO;
import com.douzone.final_backend.Service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private CommonService commonService;

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
