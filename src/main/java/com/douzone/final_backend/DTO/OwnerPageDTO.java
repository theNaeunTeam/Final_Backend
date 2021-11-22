package com.douzone.final_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerPageDTO {
    private String o_name;
    private int total;
    private int monTotal;
    private int buyTotal;
}
