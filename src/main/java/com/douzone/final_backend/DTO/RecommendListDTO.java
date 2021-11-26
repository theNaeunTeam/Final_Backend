package com.douzone.final_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendListDTO {
    private String g_owner;
    private String g_name;
    private int g_price;
    private int g_discount;
    private String g_image;
    private String o_name;
}
