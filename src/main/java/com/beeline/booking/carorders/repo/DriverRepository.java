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

    Driver getDriverByPhone(String phone);

    //@Query(value = "select  d.* from drivers_driver d where d.id not in (select  distinct t.driver_id_id from orders_order t where (to_timestamp(t.start_time/1000) > now()) and (:startTime >= t.start_time and t.end_time >= :endTime or (t.start_time between :startTime and :endTime) )) order by d.id",
    //      nativeQuery = true)

    @Query(value = "select  d.* from drivers_driver d where d.id not in (select  distinct t.driver_id_id from orders_order t where( (t.start_time <= :startTime and t.end_time> :startTime) or (t.start_time < :endTime and t.end_time>= :endTime) )) order by d.id",
            nativeQuery = true)
    List<Driver> findFreeDriversByPeriod(@Param("startTime") Long startTime, @Param("endTime") Long endTime);
}
