package com.assessment.conferenceroom.repo;
import com.assessment.conferenceroom.model.ConferenceRoom;
import com.assessment.conferenceroom.model.ScheduledMeeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.time.LocalDateTime;

public interface ScheduledMeetingRepository extends JpaRepository<ScheduledMeeting, Long> {
    List<ScheduledMeeting> findByConferenceRoomAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual
            (ConferenceRoom conferenceRoom, LocalDateTime startTime, LocalDateTime endTime);
}


