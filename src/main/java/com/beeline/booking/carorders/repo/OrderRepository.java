package com.beeline.booking.carorders.repo;

import com.beeline.booking.carorders.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author NIsaev on 05.12.2019
 */

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT m FROM Order m WHERE m.startPoint LIKE %:filter% or m.endPoint LIKE %:filter% or m.comment LIKE %:filter% or m.userPhone LIKE %:filter%")
    List<Order> getAllByFilterText(@Param("filter") String filter);

    @Query("SELECT m FROM Order m WHERE m.driverId=:driverId and m.startTime >= :startTime and m.endTime <= :endTime")
    List<Order> getAllByDriverTimePeriod(@Param("driverId") Integer driverId, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    List<Order> getAllByUserId(Integer userId);
}
