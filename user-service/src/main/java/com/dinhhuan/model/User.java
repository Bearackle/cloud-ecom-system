package com.dinhhuan.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private Long id;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "location", nullable = true)
    private String location;
    @Column(name = "phone_number", nullable = true)
    private String phoneNumber;
    @Column(name = "gender", nullable = true)
    private String gender;
    @Column(name = "lang", nullable = true)
    private String language;
    @Column(name = "birthday", nullable = true)
    private LocalDate birthday;
    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "background_color", nullable = false)
    private String backgroundColor;
    @Column(name = "avt", nullable = true)
    private String avt;
    @OneToMany(mappedBy = "user")
    private List<Address> address;
}
