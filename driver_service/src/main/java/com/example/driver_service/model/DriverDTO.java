package com.example.driver_service.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.List;

/**
 * com.server.uber_clone.dto;
 * Created by Phuoc -19127520
 * Date 03/07/2022 - 03:14 CH
 * Description: ...
 */
@Getter
@Setter
@Embeddable
public class DriverDTO implements Serializable {
    private int id;
    private String username;
    private String fullname;
    private String phone;
    private String address;
    private String email;
    private String gender;
    //private String currentLocation;
    private String driverLicenseId;
    private String driverCitizenId;
    private String status;
    private List<Vehicle> vehicleList;

//    private double currentLatAddr;
//
//    private double currentLngAddr;
    public DriverDTO(Driver driver){
        this.username=driver.getUsername();
        DriverInfor driverInfor = driver.getDriverInfor();
        if(driverInfor!=null){
            this.fullname = driverInfor.getDriverName();
            this.phone = driverInfor.getPhoneNumber();
            this.address = driverInfor.getDriverAddress();
            this.email = driverInfor.getEmail();
            this.gender = driverInfor.getGender();
            this.driverLicenseId = driverInfor.getDriverLicenseId();
           // this.currentLocation = driverInfor.getCurrentLocation();
            this.id = driverInfor.getId();
            this.driverCitizenId = driverInfor.getCitizenId();
            this.status = driverInfor.getStatus();
            this.vehicleList=driverInfor.getVehicleList();
           // this.currentLatAddr = driverInfor.getCurrLatAddr();
           // this.currentLngAddr = driverInfor.getCurrLongAddr();
        }
    }
    public DriverDTO(DriverDTO driverDTO){
        this.username=driverDTO.getUsername();
        this.fullname = driverDTO.getFullname();
        this.phone = driverDTO.getPhone();
        this.address = driverDTO.getAddress();
        this.email = driverDTO.getEmail();
        this.gender = driverDTO.getGender();
        this.driverLicenseId = driverDTO.getDriverLicenseId();
        // this.currentLocation = driverInfor.getCurrentLocation();
        this.id = driverDTO.getId();
        this.driverCitizenId = driverDTO.getDriverCitizenId();
        this.status = driverDTO.getStatus();
        this.vehicleList=driverDTO.getVehicleList();
    }

    public DriverDTO() {

    }
}
