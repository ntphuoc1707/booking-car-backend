package com.example.user_service.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * com.server.uber_clone.models;
 * Created by Phuoc -19127520
 * Date 07/07/2022 - 02:49 CH
 * Description: ...
 */
@Entity
@Getter
@Setter
@Table(name = "driverInfor")
public class DriverInfor {

    @Id
    @Column(name = "driverId")
    private int driverId;

    @Column(name = "driverName")
    private String driverName;

    @Column(name = "driverAddress")
    private String driverAddress;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "citizenId")
    private String citizenId;

    @Column(name = "driverLicenseId")
    private String driverLicenseId;

    @Column(name = "gender")
    private String gender;

    @Column(name = "status")
    private String status;


    @OneToMany(fetch = FetchType.EAGER,mappedBy = "driverInfor")
    //@JoinColumn(name = "driver_vehicles", referencedColumnName = "driverId")
    private List<Vehicle> vehicleList;

    @OneToOne
    @MapsId
    @JoinColumn(name ="driverId")
    private Driver driver;


    @Override
    public String toString() {
        return "DriverInfor{" +
                "driverId=" + driverId +
                ", driverName='" + driverName + '\'' +
                ", driverAddress='" + driverAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", citizenId='" + citizenId + '\'' +
                ", driverLicenseId='" + driverLicenseId + '\'' +
                ", gender='" + gender + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

}
