package com.example.firebaseservice.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Driver {

    private int driverId;
    private String username;
    private String password;
    private String token;

    private DriverInfor driverInfor;

    @Override
    public String toString() {
        return "Driver{" +
                "driverId=" + driverId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", driverInfor=" + driverInfor +
                '}';
    }
}
