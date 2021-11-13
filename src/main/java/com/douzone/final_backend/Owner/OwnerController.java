package com.douzone.final_backend.Owner;

import com.douzone.final_backend.Common.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/owner")
public class OwnerController {

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private OwnerService ownerService;

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
        }catch (Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();

            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }


}
