package com.example.firebaseservice.model;


import lombok.*;

/**
 * com.server.uber_clone.models;
 * Created by Phuoc -19127520
 * Date 20/07/2022 - 03:03 CH
 * Description: ...
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VehicleAndPrice {
    private VehicleType vehicleType;
    private double price;
}
