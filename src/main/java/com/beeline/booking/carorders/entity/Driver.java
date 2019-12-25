package com.beeline.booking.carorders.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author NIsaev on 05.12.2019
 */

@Entity
@Table(name = "drivers_driver")
public class Driver implements Serializable {

    @Id
    @SequenceGenerator(name="driver_gen", sequenceName="drivers_driver_id_seq", allocationSize = 1)
    @GeneratedValue(generator="driver_gen")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "places", nullable = false)
    private Integer places;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "car_id", nullable = false)
    @JsonProperty(value = "car", required = true)
    private Integer carId;

    public Driver(){

    }

    public Driver(Integer id, String name, String surname, Integer places, String phone, String status, Integer carId) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.places = places;
        this.phone = phone;
        this.status = status;
        this.carId = carId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getPlaces() {
        return places;
    }

    public void setPlaces(Integer places) {
        this.places = places;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }
}
