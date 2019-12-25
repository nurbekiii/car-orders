package com.beeline.booking.carorders.repo;

import com.beeline.booking.carorders.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author NIsaev on 05.12.2019
 */

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {

    @Query(value = "select * from drivers_driver d where d.id not in (select distinct t.driver_id_id from orders_order t where :startTime <= t.start_time and t.end_time <= :endTime) order by d.id",
            nativeQuery = true)
    List<Driver> findFreeDriversByPeriod(@Param("startTime") Long startTime, @Param("endTime") Long endTime);
}
