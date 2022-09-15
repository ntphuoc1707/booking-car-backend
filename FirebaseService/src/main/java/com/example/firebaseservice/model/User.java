package com.example.firebaseservice.model;


import lombok.*;

import javax.persistence.*;

/**
 * com.server.uber_clone.models;
 * Created by Phuoc -19127520
 * Date 01/07/2022 - 02:03 CH
 * Description: ...
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    private int id;
    private String username;
    private String password;
    private String token;
    private UserInfor userInfor;
}
