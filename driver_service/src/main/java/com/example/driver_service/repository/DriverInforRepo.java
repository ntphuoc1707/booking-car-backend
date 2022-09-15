package com.example.driver_service.repository;

import com.example.driver_service.model.Driver;
import com.example.driver_service.model.DriverInfor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * com.example.driver_service.repository;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 11:04 SA
 * Description: ...
 */
@Repository
public interface DriverInforRepo extends JpaRepository<DriverInfor, Integer> {
    public DriverInfor findDriverInforByDriverId(int driverId);
    public Boolean existsByCitizenId(String citizenId);
    public Boolean existsByPhoneNumber(String phoneNumber);
    public Boolean existsByEmail(String email);
    public Boolean existsByDriverLicenseId(String license);
}
