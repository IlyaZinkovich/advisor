package com.routes.advisor.repository;

import com.routes.advisor.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoutesRepository extends JpaRepository<Route, Long> {

    @Query("from Route r where r.toPlace= :destination and r.date >= :after and r.date <= :before")
    List<Route> findByDestinationAndDataRange(@Param("destination") String destination,
                                              @Param("after") LocalDate after,
                                              @Param("before") LocalDate before);
}