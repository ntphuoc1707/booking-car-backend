package com.example.staff_service.repository;

import com.example.staff_service.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * com.server.uber_clone.repositories;
 * Created by Phuoc -19127520
 * Date 18/08/2022 - 12:20 SA
 * Description: ...
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
    public Trip findByTripId(int tripId);
    public List<Trip> findAllByUserId(int userId);
    public List<Trip> findAllByDriverId(int driverId);
    public List<Trip> findTop5ByUserIdOrderByTripIdDesc(int userId);
}
