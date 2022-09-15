package com.example.staff_service.repository;

import com.example.staff_service.model.DriverInfor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * com.server.uber_clone.repositories;
 * Created by Phuoc -19127520
 * Date 07/07/2022 - 04:44 CH
 * Description: ...
 */
@Repository
public interface DriverInforRepository extends JpaRepository<DriverInfor, Integer> {
    public DriverInfor findDriverInforByDriverId(int driverId);
    public Boolean existsByCitizenId(String citizenId);
    public Boolean existsByPhoneNumber(String phoneNumber);
    public Boolean existsByEmail(String email);
    public Boolean existsByDriverLicenseId(String licenseId);
}
