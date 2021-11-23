package com.douzone.final_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BannerDTO {
    private String src;
    private String altText;
    private String header;
    private String description;
    private String link;
    private int pk;
}
