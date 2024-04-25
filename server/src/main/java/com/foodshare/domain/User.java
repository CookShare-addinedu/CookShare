package com.foodshare.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "location")
    private String location;

    @Column(name = "password")
    private String password;

    @Column(name = "mobile_number", unique = true)
    private String mobileNumber;


    @Column(name = "role")
    private String role;

    @Column(name = "nick_name", unique = true)
    private String nickName;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    // Getters and Setters
}
