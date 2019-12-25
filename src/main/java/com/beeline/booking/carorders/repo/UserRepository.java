package com.beeline.booking.carorders.repo;

import com.beeline.booking.carorders.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author NIsaev on 05.12.2019
 */

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserByUserName(String userName);

    @Query("SELECT m FROM User m WHERE m.userName LIKE %:filter% or m.userName LIKE %:filter% or m.firstName LIKE %:filter% or m.lastName LIKE %:filter% or m.email LIKE %:filter%")
    List<User> getAllByFilterText( @Param("filter") String filter);
}
