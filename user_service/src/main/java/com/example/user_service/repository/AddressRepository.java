package com.example.user_service.repository;

import com.example.user_service.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * com.server.uber_clone.repositories;
 * Created by Phuoc -19127520
 * Date 23/08/2022 - 11:43 SA
 * Description: ...
 */
@Repository
public interface AddressRepository extends JpaRepository<Address,Integer> {
    Address findByAddressAndLatitudeAndLongitudeAndUserId(String address, Double latitude, Double longitude, int userId);
    List<Address> findTop5ByUserIdOrderByPickNumDesc(int userId);
}
