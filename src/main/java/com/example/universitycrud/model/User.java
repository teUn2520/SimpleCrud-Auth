package com.example.universitycrud.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name= "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
}
