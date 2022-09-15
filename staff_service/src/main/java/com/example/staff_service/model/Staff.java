package com.example.staff_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * com.server.uber_clone.models;
 * Created by Phuoc -19127520
 * Date 02/07/2022 - 09:49 CH
 * Description: ...
 */
@Entity
@Getter
@Setter
@Table(name = "staff")
public class Staff {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private StaffType type;

    @Column(name = "token")
    private String token;

    @JsonIgnore
//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "staff_infor", referencedColumnName = "id")
    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private StaffInfor staffInfor;

}

