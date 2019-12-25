package com.beeline.booking.carorders.pojo;

import java.io.Serializable;

/**
 * @author NIsaev on 18.12.2019
 */
public class SmsSendRequest implements Serializable {

    private String login;
    private String password;
    private String msisdn;
    private String text;

    public SmsSendRequest(){

    }

    public SmsSendRequest(String login, String password, String msisdn, String text) {
        this.login = login;
        this.password = password;
        this.msisdn = msisdn;
        this.text = text;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
