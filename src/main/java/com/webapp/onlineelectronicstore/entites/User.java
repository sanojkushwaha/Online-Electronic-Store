package com.webapp.onlineelectronicstore.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name="users")
public class User {

    @Id
    @Column(name ="user_id")
    private String userId;

    @Column(name ="user_name")
    private String name;

    @Column(name="user_email", unique=true, length=60)
    private String email;

    @Column(name="user_password", length=10)
    private String password;

    private String gender;

    @Column(length = 1000)
    private String about;

    @Column(name="user_image_name", length=100)
    private String imageName;

}
