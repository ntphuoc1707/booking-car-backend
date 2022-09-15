package com.example.driver_service.repository;

import com.example.driver_service.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * com.example.driver_service.repository;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 11:03 SA
 * Description: ...
 */
@Repository
public interface DriverRepo extends JpaRepository<Driver,Integer> {
    Driver findDriverByUsername(String username);
    Boolean existsByUsername(String username);
    Driver findDriverById(int id);
    public Driver findDriverByUsernameAndPassword(String Username, String Password);
    public List<Driver> findAll();
}
