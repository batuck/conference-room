package com.assessment.conferenceroom.repo;
import com.assessment.conferenceroom.model.ConferenceRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoom, Long> {
    List<ConferenceRoom> findByCapacityGreaterThanEqualOrderByCapacityAsc(int capacity);
}

