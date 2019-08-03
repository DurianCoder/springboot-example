package com.example.serviceprovider.mysql.domain.bigdata;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    private Integer id;

    private String userName;

    private String userPwd;

}