package com.douzone.final_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocalDTO {

    private int seoul;
    private int daejeon;
    private int daegu;
    private int busan;
    private int ulsan;
    private int gwangju;
    private int jeju;
    private int gyeongsang;
    private int gangwon;
    private int chungcheong;
    private int jeonla;
    private int gyeonggi;
    private int incheon;

}
