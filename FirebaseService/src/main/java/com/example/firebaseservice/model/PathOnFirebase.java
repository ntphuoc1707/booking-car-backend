package com.example.firebaseservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * com.server.uber_clone.firebase;
 * Created by Phuoc -19127520
 * Date 11/08/2022 - 03:38 CH
 * Description: ...
 */

@Getter
@Setter
@Entity
@Table(name = "pathOnFirebase")
public class PathOnFirebase {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "type")
    private String type;

    @Column(name = "idRequestOrDriver") //ID of Request or Driver
    private Integer idRrD;

    @Column(name = "path")
    private String path;

}
