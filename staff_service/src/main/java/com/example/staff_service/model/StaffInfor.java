package com.example.staff_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * com.server.uber_clone.models;
 * Created by Phuoc -19127520
 * Date 07/07/2022 - 04:21 CH
 * Description: ...
 */
@Entity
@Getter
@Setter
@Table(name = "staffInfor")
public class StaffInfor {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private String gender;

    @JsonIgnore
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Staff staff;
}
