package com.example.user_service.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * com.server.uber_clone.dto;
 * Created by Phuoc -19127520
 * Date 03/07/2022 - 02:55 CH
 * Description: ...
 */
@Getter
@Setter
public class UserDTO implements Serializable {
    private Integer userId;
    private String username;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String gender;
    private String homeAddress;

    public UserDTO(User user) {
        this.userId=user.getId();
        this.username = user.getUsername();
        UserInfor userInfo = user.getUserInfor();
        if(userInfo != null) {
            this.homeAddress = userInfo.getHomeAddress();
            this.gender = userInfo.getGender();
            this.phoneNumber = userInfo.getPhoneNumber();
            this.email = userInfo.getEmail();
            this.fullName = userInfo.getFullName();
        }
    }
}
