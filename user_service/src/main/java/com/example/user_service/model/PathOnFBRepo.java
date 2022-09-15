package com.example.user_service.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * com.server.uber_clone.repositories;
 * Created by Phuoc -19127520
 * Date 11/08/2022 - 03:45 CH
 * Description: ...
 */
public interface PathOnFBRepo extends JpaRepository<PathOnFirebase, Integer> {
    public PathOnFirebase findByTypeAndAndIdRrD(String type, int idRrD);
}
