package com.example.staff_service.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * com.server.uber_clone.models;
 * Created by Phuoc -19127520
 * Date 10/08/2022 - 03:07 CH
 * Description: ...
 */

@Getter
@Setter
@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    public WalletType walletType;

    @Column(name = "idOwner")
    public Integer idOwner;

    @Column(name = "balance")
    public Double balance;

    @Column(name="activecode")
    public Integer activeCode;

    @Column(name = "status")
    public Boolean status;
}
