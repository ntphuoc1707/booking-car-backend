package com.example.driver_service.repository;

import com.example.driver_service.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * com.server.uber_clone.repositories;
 * Created by Phuoc -19127520
 * Date 15/07/2022 - 11:25 CH
 * Description: ...
 */
public interface RequestRepository extends JpaRepository<Request, Integer> {
    public Request findRequestByRequestId(int requestId);
}
