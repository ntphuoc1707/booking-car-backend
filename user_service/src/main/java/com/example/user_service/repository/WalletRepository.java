package com.example.user_service.repository;

import com.example.user_service.model.Wallet;
import com.example.user_service.model.WalletType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * com.server.uber_clone.repositories;
 * Created by Phuoc -19127520
 * Date 10/08/2022 - 03:33 CH
 * Description: ...
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    Boolean existsByWalletTypeAndAndIdOwner(WalletType walletType, int idOwner);
    Wallet findByIdOwnerAndWalletType(int idOwner, WalletType walletType);
}
