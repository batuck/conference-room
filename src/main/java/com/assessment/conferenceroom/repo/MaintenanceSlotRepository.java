package com.assessment.conferenceroom.repo;

import com.assessment.conferenceroom.model.MaintenanceSlot;
import com.assessment.conferenceroom.model.ConferenceRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;

public interface MaintenanceSlotRepository extends JpaRepository<MaintenanceSlot, Long> {
    List<MaintenanceSlot> findByRoomAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(ConferenceRoom room,LocalTime startTime, LocalTime endTime);

    List<MaintenanceSlot> findByStartTimeLessThanEqualAndEndTimeGreaterThanEqual
            (LocalTime startTime, LocalTime endTime);
}

