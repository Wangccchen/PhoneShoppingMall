package com.wangchen.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int userID;
    private String username;
    private String password;
    private String email;
    private String address;
    private String phoneNumber;

}

