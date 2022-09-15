package com.example.driver_service.repository;

import com.example.driver_service.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * com.example.driver_service.repository;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 11:09 SA
 * Description: ...
 */
@Repository
public interface VehicleRepo extends JpaRepository<Vehicle,Integer> {
    public List<Vehicle> findAll();
    public Boolean existsByDriverIdAndLicensePlateNum(int driverId, String licensePlateNum);
    public List<Vehicle> findVehicleByDriverId(int driverId);
    public Boolean existsVehicleByLicensePlateNum(String licensePlateNum);
    Boolean existsByLicensePlateNum(String license);
    public List<Vehicle> findVehicleByStatus(Boolean status);
    public Vehicle findVehicleByLicensePlateNum(String licensePlateNum);
    public List<Vehicle> findAllByStatus(boolean status);
}
