package com.beeline.booking.carorders.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author NIsaev on 16.12.2019
 */
public class UserReg implements Serializable {
    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "password")
    private String password;

    public UserReg() {
    }

    public UserReg(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
