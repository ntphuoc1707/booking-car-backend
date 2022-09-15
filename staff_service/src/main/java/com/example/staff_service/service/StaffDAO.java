package com.example.staff_service.service;

import com.example.staff_service.model.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * com.example.staff_service.service;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 06:02 CH
 * Description: ...
 */
public interface StaffDAO extends UserDetailsService {
    Result login(Staff staffLogin);
    Result signup(Staff staffSignup);
    Result getStaffByUsername(String username);
    Result getAllUser();
    Result getAllDriver();
    Result getAllVehicle();
    Result getAllStaff();
    Result addDiscount(Discount discount);
    Result getAllDiscount();
    Result editDiscount(Discount discount);
    Result deleteDiscountById(int discountId);
    Result booking(Request request);
    Result getAllRequest();
    Result getRequestVehicle();
    Result acceptVehicle(String licensePlateNum);
    Result getVehicleAndPrice(DistanceAndTime distanceAndTime);
    Result cancelBooking(int requestId);
    Result getVehicleOfDriver(int driverId);
    Result changePassword(String username, String password, String rePassword);
    Staff getStaffById(int id);
    String refreshToken(int id);
    Result editStaffInfo(String token, StaffInfor editInfor);
    Result deleteStaffById(int staffId);
    Result getAllTrip();
    Result get5latestTripForUser(int userId);
    Result getAllTripByDriver(int driverId);
    Result get5mostDesByUser(int userId);
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    UserDetails loadStaffById(int id) throws UsernameNotFoundException;
}
