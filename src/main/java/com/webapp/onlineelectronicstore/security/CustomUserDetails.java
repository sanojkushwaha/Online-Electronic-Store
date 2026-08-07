package com.webapp.onlineelectronicstore.security;

import com.webapp.onlineelectronicstore.entites.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    //constructor injection
    private final User user;
    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * Returns the role of the logged-in user.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority(user.getRole().name())
        );
    }
    /**
     * Returns the encrypted password.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Username used for login.
     * Here we are using Email.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Account is not expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Account is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Credentials are valid.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Account is enabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    // Optional: Return the complete User object
    public User getUser() {
        return user;
    }
}