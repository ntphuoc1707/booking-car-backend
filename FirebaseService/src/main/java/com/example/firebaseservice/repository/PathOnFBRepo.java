package com.example.firebaseservice.repository;

import com.example.firebaseservice.model.PathOnFirebase;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * com.example.firebaseservice.repository;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 10:33 CH
 * Description: ...
 */
public interface PathOnFBRepo extends JpaRepository<PathOnFirebase, Integer> {
    PathOnFirebase findByTypeAndIdRrD(String typeList, int id);
}
