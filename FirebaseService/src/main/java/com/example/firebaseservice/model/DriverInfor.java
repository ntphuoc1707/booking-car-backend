package com.example.firebaseservice.model;

import lombok.*;

import java.util.List;

/**
 * com.server.uber_clone.models;
 * Created by Phuoc -19127520
 * Date 07/07/2022 - 04:45 CH
 * Description: ...
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverInfor {
    private int driverId;
    private String driverName;
    private String driverAddress;
    private String phoneNumber;
    private String email;
    private String citizenId;
    private String driverLicenseId;
    private String gender;
    private String status;
    private List<Vehicle> vehicleList;
    private Driver driver;
    private List<Trip> trips;

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
