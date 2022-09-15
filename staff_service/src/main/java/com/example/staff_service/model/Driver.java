package com.example.staff_service.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Driver")
public class Driver {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "driverId")
    private int driverId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "token")
    private String token;

//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name="driver_infor", referencedColumnName = "driverId")

    @OneToOne(mappedBy = "driver", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private DriverInfor driverInfor;

    @Override
    public String toString() {
        return "Driver{" +
                "driverId=" + driverId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", driverInfor=" + driverInfor +
                '}';
    }
}
