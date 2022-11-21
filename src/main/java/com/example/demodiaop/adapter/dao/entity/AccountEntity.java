package com.example.demodiaop.adapter.dao.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "account")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
}
