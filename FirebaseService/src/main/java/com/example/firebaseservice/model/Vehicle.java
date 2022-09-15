package com.example.firebaseservice.model;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Vehicle implements Serializable {

    private String licensePlateNum;
    private int driverId;
    private String ownername;
    private VehicleType typeOfVehicle;
    private String conditionVehicle;
    private String brand;
    private Boolean status;
    private DriverInfor driverInfor;

    @Override
    public String toString() {
        return "Vehicle{" +
                "licensePlateNum='" + licensePlateNum + '\'' +
                ", driverId=" + driverId +
                ", ownername='" + ownername + '\'' +
                ", typeOfVehicle=" + typeOfVehicle +
                ", conditionVehicle='" + conditionVehicle + '\'' +
                ", brand='" + brand + '\'' +
                ", status=" + status +
                '}';
    }
}
