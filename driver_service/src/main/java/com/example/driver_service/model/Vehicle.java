package com.example.driver_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * com.example.driver_service.model;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 11:07 SA
 * Description: ...
 */
@Getter
@Setter
@Entity
@Table(name = "vehicle")
public class Vehicle implements Serializable {

    @Id
    @Column(name = "licensePlateNum")
    private String licensePlateNum;

    @Column(name = "driverId")
    private int driverId;

    @Column(name="ownername")
    private String ownername;

    @Enumerated(EnumType.STRING)
    @Column(name = "typeOfVehicle")
    private VehicleType typeOfVehicle;

    @Column(name = "conditionVehicle")
    private String conditionVehicle;

    @Column(name = "brand")
    private String brand;

    @Column(name = "status")
    private Boolean status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="driverId", insertable = false, updatable = false)
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