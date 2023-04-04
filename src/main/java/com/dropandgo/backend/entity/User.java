package com.dropandgo.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long filesUploaded;
    private Long filesDownloaded;
    private String email;
    private String password;

    public User(String name, Long filesUploaded, Long filesDownloaded, String email, String password) {
        this.name = name;
        this.filesUploaded = filesUploaded;
        this.filesDownloaded = filesDownloaded;
        this.email = email;
        this.password = password;
    }
}
