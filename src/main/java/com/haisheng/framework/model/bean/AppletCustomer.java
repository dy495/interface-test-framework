package com.haisheng.framework.model.bean;


import lombok.Data;

@Data
public class AppletCustomer {

    private String id;   //微信id
    private int is_bind_user;
    private String customerName;
    private String phone;
    private Long customer_id;

}
