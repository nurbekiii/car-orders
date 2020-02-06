package com.beeline.booking.carorders.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author NIsaev on 05.12.2019
 */
public class Period implements Serializable {
    @JsonProperty(value = "start_time", required = true)
    private Long startTime;

    @JsonProperty(value = "end_time", required = true)
    private Long endTime;

    public Period() {

    }

    public Period(Long startTime, Long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
