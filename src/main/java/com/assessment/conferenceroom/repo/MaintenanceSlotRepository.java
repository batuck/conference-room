package com.assessment.conferenceroom.repo;

import com.assessment.conferenceroom.model.MaintenanceSlot;
import com.assessment.conferenceroom.model.ConferenceRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface MaintenanceSlotRepository extends JpaRepository<MaintenanceSlot, Long> {
    @Query("SELECT m FROM MaintenanceSlot m " +
            "WHERE :startTime BETWEEN m.startTime AND m.endTime " +
            "OR :endTime BETWEEN m.startTime AND m.endTime " +
            "OR m.startTime BETWEEN :startTime AND :endTime " +
            "OR m.endTime BETWEEN :startTime AND :endTime")
    List<MaintenanceSlot> findOverlappingSlots(@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);
}

