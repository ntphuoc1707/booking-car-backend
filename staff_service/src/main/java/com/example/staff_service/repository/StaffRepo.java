package com.example.staff_service.repository;

import com.example.staff_service.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * com.example.staff_service.repository;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 05:58 CH
 * Description: ...
 */
public interface StaffRepo extends JpaRepository<Staff,Integer> {
    Staff findStaffByUsername(String username);
    Boolean existsByUsername(String username);
    Staff findStaffById(int id);
}
