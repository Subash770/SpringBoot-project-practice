package com.example.cabBooking.repository;

import com.example.cabBooking.entity.RideDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RideDetailsRepository extends JpaRepository<RideDetails, Long> {

    List<RideDetails> findByUserId(Long userId);

    List<RideDetails> findByStartTimeAfter(LocalDateTime time);
}



