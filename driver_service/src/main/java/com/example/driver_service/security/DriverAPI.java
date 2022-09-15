package com.example.driver_service.security;

/**
 * com.server.uber_clone.security.path.driver;
 * Created by Phuoc -19127520
 * Date 29/07/2022 - 04:03 CH
 * Description: ...
 */
public record DriverAPI() {
    public static final String[] driverPublicAPI={
            "/driver/login",
            "/driver/signup",
            "/driver/checkPhoneNumber",
            "/driver/checkVehiclePlateNum",
            "/driver/checkDriverInfo"};
}
