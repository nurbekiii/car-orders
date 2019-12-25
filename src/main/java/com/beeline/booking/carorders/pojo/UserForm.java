package com.beeline.booking.carorders.pojo;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author NIsaev on 23.12.2019
 */
public class UserForm {
    private String username;
    private Collection<? extends GrantedAuthority> authorities;

    public UserForm() {

    }

    public UserForm(String username, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
