package com.example.user_service.service;

import com.example.user_service.model.DistanceAndTime;
import com.example.user_service.model.Request;
import com.example.user_service.model.Result;
import com.example.user_service.model.User;
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
public interface UserDAO extends UserDetailsService {
    Result login(User userLogin);
    Result signup(User userSignup);
    Result getUserByUsername(String username);
    Result checkPhoneNumber(String phoneNumber);
    Result checkEmail(String email);
    String refreshToken(int id);
    //Result getUserById(int id);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    UserDetails loadUserById(int id) throws UsernameNotFoundException;
    Result booking(int userId, Request request);
    Result getVehicleAndPrice(DistanceAndTime distanceAndTime);
    Result getUserInforByToken(String token);
    Result getUserByToken(String token);
    //    Result confirmBooking(ConfirmBooking confirmBooking);
    Result cancelBooking(int requestId);
    User getUserById(int id);
    Boolean checkId(int id);
    Result getListDiscount();
    Result getWallet(String token);
    Result recharge(String token, double money);
    Result withdraw(String token, double money);
    Result getLinkAfterBooking(int requestId);
}
