package com.example.staff_service.model;


import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * com.server.uber_clone.models;
 * Created by Phuoc -19127520
 * Date 20/07/2022 - 03:03 CH
 * Description: ...
 */

@Embeddable
public class VehicleAndPrice {

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
    private double price;

    public VehicleAndPrice(VehicleType vehicleType, double price) {
        this.vehicleType = vehicleType;
        this.price = price;
    }

    public VehicleAndPrice() {

    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
