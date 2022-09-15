package com.example.staff_service.model;

import java.util.ArrayList;
import java.util.List;


/**
 * com.server.uber_clone.models;
 * Created by Phuoc -19127520
 * Date 16/07/2022 - 03:26 CH
 * Description: ...
 */
public class ResponseBooking {

    private List<VehicleAndPrice> vehiclesAndPrices;

    public ResponseBooking(double distance, int timeSecond){
        this.vehiclesAndPrices=new ArrayList<>();
        this.vehiclesAndPrices.add(new VehicleAndPrice(VehicleType.MOTORBIKE,generatePrice(distance,VehicleType.MOTORBIKE, timeSecond)));
        this.vehiclesAndPrices.add(new VehicleAndPrice(VehicleType.CAR4S,generatePrice(distance,VehicleType.CAR4S, timeSecond)));
        this.vehiclesAndPrices.add(new VehicleAndPrice(VehicleType.CAR7S,generatePrice(distance,VehicleType.CAR7S, timeSecond)));
        this.vehiclesAndPrices.add(new VehicleAndPrice(VehicleType.CAR16S,generatePrice(distance,VehicleType.CAR16S, timeSecond)));
    }

    public List<VehicleAndPrice> getVehiclesAndPrices() {
        return vehiclesAndPrices;
    }

    public void setVehiclesAndPrices(List<VehicleAndPrice> vehiclesAndPrices) {
        this.vehiclesAndPrices = vehiclesAndPrices;
    }

    private double generatePrice(double distance, VehicleType vehicleType, int timeSecond){
        double price=0;
        switch (vehicleType){
            case MOTORBIKE:
                if(distance<=2){
                    price=12500;
                }
                else{
                    price=12500+(distance-2)*4300+((double)timeSecond/60)*350;
                }
                break;
            case CAR4S:
                if(distance<=2){
                    price=29000;
                }
                else{
                    price=29000+(distance-2)*10000+((double)timeSecond/60)*450;
                }
                break;
            case CAR7S:
                if(distance<=2){
                    price=34000;
                }
                else{
                    price=34000+(distance-2)*13000+((double)timeSecond/60)*550;
                }
                break;
            case CAR16S:
                if(distance<=2){
                    price=40000;
                }
                else{
                    price=40000+(distance-2)*15000+((double)timeSecond/60)*700;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + vehicleType);
        }
        return Math.round(price*1000)/1000;
    }
}
