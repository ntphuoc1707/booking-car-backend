package com.example.user_service.repository;

import com.example.user_service.model.UserInfor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * com.example.user_service.repository;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 10:40 SA
 * Description: ...
 */
public interface UserInforRepo extends JpaRepository<UserInfor, Integer> {
    public Boolean existsByPhoneNumber(String phoneNumber);
    public Boolean existsByEmail(String email);
    public UserInfor findUserInforById(int id);
}
