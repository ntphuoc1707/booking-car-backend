package com.example.staff_service.model;

import java.io.Serializable;

public class vehicleDTO implements Serializable {
    private String licensePlateNum;

    private int driverId;

    private VehicleType typeOfVehicle;

    private String conditionVehicle;

    private String brand;

    private Boolean status;

    private String driverName;

    public vehicleDTO(Vehicle vehicle, String driverName){
        this.licensePlateNum = vehicle.getLicensePlateNum();
        this.brand = vehicle.getBrand();
        this.conditionVehicle = vehicle.getConditionVehicle();
        this.typeOfVehicle = vehicle.getTypeOfVehicle();
        this.status = vehicle.getStatus();
        this.driverId = vehicle.getDriverId();
        this.driverName = driverName;
    }

    public int getDriverId() {
        return driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getBrand() {
        return brand;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getConditionVehicle() {
        return conditionVehicle;
    }

    public String getLicensePlateNum() {
        return licensePlateNum;
    }

    public VehicleType getTypeOfVehicle() {
        return typeOfVehicle;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setTypeOfVehicle(VehicleType typeOfVehicle) {
        this.typeOfVehicle = typeOfVehicle;
    }

    public void setLicensePlateNum(String licensePlateNum) {
        this.licensePlateNum = licensePlateNum;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setConditionVehicle(String conditionVehicle) {
        this.conditionVehicle = conditionVehicle;
    }


}
