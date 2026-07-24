package com.rentaltaxi.security;

import com.rentaltaxi.entity.Admin;
import com.rentaltaxi.entity.Customer;
import com.rentaltaxi.entity.Driver;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {

    private final Integer id;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    private UserPrincipal(Integer id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrincipal create(Customer customer) {
        return new UserPrincipal(
                customer.getCustomerId(),
                customer.getUsername(),
                customer.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
        );
    }

    public static UserPrincipal create(Driver driver) {
        return new UserPrincipal(
                driver.getDriverId(),
                driver.getUsername(),
                driver.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_DRIVER"))
        );
    }

    public static UserPrincipal create(Admin admin) {
        return new UserPrincipal(
                admin.getAdminId(),
                admin.getUsername(),
                admin.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }

    public Integer getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
