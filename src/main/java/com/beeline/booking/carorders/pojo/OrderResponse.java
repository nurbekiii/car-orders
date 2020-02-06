package com.beeline.booking.carorders.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

/**
 * @author NIsaev on 06.12.2019
 */

@JsonIgnoreProperties(ignoreUnknown = true, value = {"created_at", "updated_at"})
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderResponse implements Serializable {

    @JsonProperty(value = "id")
    private Integer id;

    @JsonProperty(value = "start_time", required = true)
    private Long startTime;

    @JsonProperty(value = "end_time", required = true)
    private Long endTime;

    @JsonProperty(value = "start_point", required = true)
    private String startPoint;

    @JsonProperty(value = "end_point", required = true)
    private String endPoint;

    @JsonProperty(value = "user_phone", required = true)
    private String userPhone;

    @JsonProperty(value = "comment")
    private String comment;

    @JsonProperty(value = "user1", required = true)
    private Integer user_id;

    @JsonProperty(value = "driver_id", required = true)
    private JsonNode driverId;

    public OrderResponse() {

    }

    public OrderResponse(Integer id, Long startTime, Long endTime, String startPoint, String endPoint, String userPhone, String comment, Integer user_id, JsonNode driverId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.userPhone = userPhone;
        this.comment = comment;
        this.user_id = user_id;
        this.driverId = driverId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public JsonNode getDriverId() {
        return driverId;
    }

    public void setDriverId(JsonNode driverId) {
        this.driverId = driverId;
    }
}
