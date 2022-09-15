package com.example.staff_service.repository;

import com.example.staff_service.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * com.server.uber_clone.repositories.vehicle;
 * Created by Phuoc -19127520
 * Date 10/07/2022 - 09:50 CH
 * Description: ...
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    public List<Vehicle> findAll();
    public Boolean existsByDriverIdAndLicensePlateNum(int driverId, String licensePlateNum);
    public List<Vehicle> findVehicleByDriverId(int driverId);
    public Boolean existsVehicleByLicensePlateNum(String licensePlateNum);
    public List<Vehicle> findVehicleByStatus(Boolean status);
    public Vehicle findVehicleByLicensePlateNum(String licensePlateNum);
    public List<Vehicle> findAllByStatus(boolean status);
}
