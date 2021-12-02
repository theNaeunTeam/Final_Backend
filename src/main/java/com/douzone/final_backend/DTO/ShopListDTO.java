package com.douzone.final_backend.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopListDTO {

    private String radius;
    private String o_sNumber;
    private String o_phone;
    private String o_cellPhone;
    private String o_name;
    private String o_address;
    private String o_latitude;
    private String o_longitude;
    private String o_time1;
    private String o_time2;
    private String o_image;
    private String distance;
    private int startIndex;
    private String goodsName;
    private int totalGoodsCnt;
    private int searchResult;
}
