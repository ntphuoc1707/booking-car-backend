package com.example.driver_service.service;

import com.example.driver_service.model.Driver;
import com.example.driver_service.model.DriverInfor;
import com.example.driver_service.model.Result;
import com.example.driver_service.model.Vehicle;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * com.example.user_service.service;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 10:38 SA
 * Description: ...
 */
public interface DriverDAO extends UserDetailsService {
    Result login(Driver driverLogin);
    Result signup(Driver driverSignup);
    Result getDriverByUsername(String username);
    Result addVehicle(int driverId, Vehicle vehicle);
    Result getVehicle(int driverId);
    Driver getDriverById(int id);
    String refreshToken(int id);
    Result isExistPhoneNumber(String phoneNumber);
    Result checkDriverInfo(DriverInfor driverInfor);
    Result checkVehiclePlateNum(String vehiclePlateNum);
    Result getDriverByToken(String token);
    Result getWallet(String token);
    Result recharge(String token, double money);
    Result withdraw(String token, double money);
    Result getFeedBacks(String token);
    Result getTrips(String token);
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    UserDetails loadDriverById(int id) throws UsernameNotFoundException;
}
