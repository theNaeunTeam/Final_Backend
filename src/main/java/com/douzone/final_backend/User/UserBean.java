package com.douzone.final_backend.User;

import lombok.Data;

@Data
public class UserBean {
    private String u_id;
    private String u_pw;
    private String u_cellPhone;
    private  String u_email;
    private String u_gender;
    private int u_age;
    private int u_status;
    private int u_noShowCnt;
}
