package com.example.staff_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "address")
public class Address {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "addressId")
    private int addressId;

    @Column(name = "userId")
    private int userId;

    @Column(name = "address")
    private String address;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "pickNum")
    private int pickNum;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userId",insertable = false,updatable = false)
    private UserInfor userInfor;

    public void setAddress(Position position){
        address=position.getAddress();
        latitude=position.getLatitude();
        longitude=position.getLongitude();
    }

}
