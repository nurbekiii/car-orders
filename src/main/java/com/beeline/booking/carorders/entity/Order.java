package com.beeline.booking.carorders.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author NIsaev on 05.12.2019
 */

@Entity
@Table(name = "orders_order")
public class Order implements Serializable {

    @Id
    @SequenceGenerator(name = "order_gen", sequenceName = "orders_order_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "order_gen")
    private Integer id;

    @Column(name = "start_time", nullable = false)
    @JsonProperty(value = "start_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Long startTime;

    @Column(name = "end_time", nullable = false)
    @JsonProperty(value = "end_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Long endTime;

    @Column(name = "start_point", nullable = false)
    @JsonProperty(value = "start_point")
    private String startPoint;

    @Column(name = "end_point", nullable = false)
    @JsonProperty(value = "end_point")
    private String endPoint;

    @Column(name = "comment", nullable = false)
    @JsonProperty(value = "comment")
    private String comment;

    @Column(name = "user_phone", nullable = false)
    @JsonProperty(value = "user_phone")
    private String userPhone;

    @Column(name = "user1_id", nullable = false)
    @JsonProperty(value = "user_id")
    private Integer userId;

    @Column(name = "driver_id_id", nullable = false)
    @JsonProperty(value = "driver_id")
    private Integer driverId;

    public Order() {

    }

    public Order(Integer id, Long startTime, Long endTime, String startPoint, String endPoint, String comment, String userPhone, Integer userId, Integer driverId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.comment = comment;
        this.userPhone = userPhone;
        this.userId = userId;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }
}
