package com.example.user_service.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * com.server.uber_clone.models;
 * Created by Phuoc -19127520
 * Date 07/07/2022 - 02:49 CH
 * Description: ...
 */
@Entity
@RedisHash("user_infor")
@Getter
@Setter
@Table(name = "userInfor")
public class UserInfor {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "homeAddress")
    private String homeAddress;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "fullName")
    private String fullName;

    @JsonIgnore
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

}
