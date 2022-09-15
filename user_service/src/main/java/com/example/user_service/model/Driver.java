package com.example.user_service.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * com.server.uber_clone.models;
 * Created by Phuoc -19127520
 * Date 01/07/2022 - 02:03 CH
 * Description: ...
 */
@Entity
@Getter
@Setter
@Table(name = "driver")
public class Driver implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "token")
    private String token;

//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_infor", referencedColumnName = "id")
    @OneToOne(mappedBy = "driver", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private DriverInfor driverInfor;
}
