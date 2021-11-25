package com.douzone.final_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDTO {
    private String date;
    private int sum;
    private int tal;


    List<SaleDTO> day;
    List<SaleDTO> mon;
    List<SaleDTO> year;


    List<Object> totalMon;
    List<Object> totalYear;

    List<Object> d;
    List<Object> m;


}
