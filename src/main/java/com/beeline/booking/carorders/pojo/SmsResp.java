package com.beeline.booking.carorders.pojo;

import java.io.Serializable;

/**
 * @author NIsaev on 18.12.2019
 */
public class SmsResp  implements Serializable {
    private String status;
    private String message;

    public SmsResp(){

    }

    public SmsResp(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SmsResp{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
