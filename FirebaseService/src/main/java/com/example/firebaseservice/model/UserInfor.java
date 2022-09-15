package com.example.firebaseservice.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

/**
 * com.server.uber_clone.models;
 * Created by Phuoc -19127520
 * Date 07/07/2022 - 02:49 CH
 * Description: ...
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInfor {

    private int id;
    private String homeAddress;
    private String gender;
    private String phoneNumber;
    private String email;
    private String fullName;
    private User user;

}
