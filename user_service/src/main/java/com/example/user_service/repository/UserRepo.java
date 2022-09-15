package com.example.user_service.repository;

import com.example.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * com.example.user_service.repository;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 10:34 SA
 * Description: ...
 */
@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    User findUserByUsername(String username);
    Boolean existsByUsername(String username);
    User findUserById(int id);
    User getUserById(int id);
}
