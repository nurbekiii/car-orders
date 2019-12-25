package com.beeline.booking.carorders.pojo;

/**
 * @author NIsaev on 17.12.2019
 */
public class UserFilter {
    private String textvalue;

    public UserFilter() {}

    public UserFilter(String textvalue) {
        this.textvalue = textvalue;
    }

    public String getTextvalue() {
        return textvalue;
    }

    public void setTextvalue(String textvalue) {
        this.textvalue = textvalue;
    }
}
