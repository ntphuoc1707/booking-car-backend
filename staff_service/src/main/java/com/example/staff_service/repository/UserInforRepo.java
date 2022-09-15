package com.example.staff_service.repository;

import com.example.staff_service.model.UserInfor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * com.example.user_service.repository;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 10:40 SA
 * Description: ...
 */
public interface UserInforRepo extends JpaRepository<UserInfor, Integer> {
    UserInfor findUserInforById(int id);
}
