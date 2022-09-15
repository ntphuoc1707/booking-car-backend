package com.example.staff_service.repository;

import com.example.staff_service.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * com.server.uber_clone.repositories;
 * Created by Phuoc -19127520
 * Date 02/07/2022 - 10:12 CH
 * Description: ...
 */
@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    public Driver findDriverByDriverId(int driverId);
    public Driver findDriverByUsernameAndPassword(String Username, String Password);
    public Driver findDriverByUsername(String Username);
    public Boolean existsByUsername(String Username);
    public List<Driver> findAll();
}
