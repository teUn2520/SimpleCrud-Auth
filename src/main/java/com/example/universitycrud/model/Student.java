package com.example.universitycrud.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
public class Student implements Serializable{
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    @Column(unique = true)
    private String email;
    @Transient
    private int age;
}
