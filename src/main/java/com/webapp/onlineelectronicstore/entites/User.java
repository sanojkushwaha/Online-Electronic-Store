package com.webapp.onlineelectronicstore.entites;

import com.webapp.onlineelectronicstore.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name="users")
public class User implements UserDetails {

    @Id
    @Column(name ="user_id")
    private String userId;

    @Column(name ="user_name")
    private String name;

    @Column(name="user_email", unique=true, length=60)
    private String email;

    @Column(name="user_password", length=500)
    private String password;

    private String gender;

    @Column(length = 1000)
    private String about;

    @Column(name="user_image_name", length=100)
    private String imageName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    //Implement the UserDetails interface method
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getUsername() {
        return this.email;   // or email field
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
