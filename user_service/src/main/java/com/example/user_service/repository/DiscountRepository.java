package com.example.user_service.repository;

import com.example.user_service.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * com.server.uber_clone.repositories.staff;
 * Created by Phuoc -19127520
 * Date 09/07/2022 - 11:15 CH
 * Description: ...
 */
public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    public List<Discount> findAll();
    public void deleteByDiscountId(int discountId);
    public Boolean existsDiscountByDiscountId(int discountId);

    public Discount findByDiscountId(int discountId);
}
