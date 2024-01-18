package com.assessment.conferenceroom.service.impl;

import com.assessment.conferenceroom.dto.BookingDTO;
import com.assessment.conferenceroom.model.ConferenceRoom;
import com.assessment.conferenceroom.model.MaintenanceSlot;
import com.assessment.conferenceroom.model.ScheduledMeeting;
import com.assessment.conferenceroom.repo.ConferenceRoomRepository;
import com.assessment.conferenceroom.repo.MaintenanceSlotRepository;
import com.assessment.conferenceroom.repo.ScheduledMeetingRepository;
import com.assessment.conferenceroom.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class SchedulerServiceImpl implements SchedulerService {
    @Autowired
    MaintenanceSlotRepository maintenanceSlotRepository;
    @Autowired
    ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    ScheduledMeetingRepository scheduledMeetingRepository;

    @Override
    public String checkAndCreateSchedule(BookingDTO bookingDTO) {
        if (isMaintenanceSlot(bookingDTO.getStartDateTime().toLocalTime(), bookingDTO.getStartDateTime().toLocalTime())) {
            return "Cannot schedule a meeting during maintenance period.";
        }

        // Find available room and time slot
        ConferenceRoom conferenceRoom = findAvailableRoom(bookingDTO.getStartDateTime(), bookingDTO.getStartDateTime(), bookingDTO.getHeadcount());
        if (conferenceRoom != null) {
            if (bookingDTO.getIsCreate()) {
                ScheduledMeeting meeting = new ScheduledMeeting();
                meeting.setStartTime(bookingDTO.getStartDateTime());
                meeting.setEndTime(bookingDTO.getEndDateTime());
                meeting.setHeadcount(bookingDTO.getHeadcount());

                // Assuming room_id is set based on availability
                meeting.setConferenceRoom(conferenceRoom);

                scheduledMeetingRepository.save(meeting);
                return "Meeting scheduled successfully.";
            } else {
                return conferenceRoom.getConferenceRoomName()+" is available";
            }
        } else {
            return "No available room or time slot found.";
        }
    }

    private ConferenceRoom findAvailableRoom(LocalDateTime startTime, LocalDateTime endTime, int numberOfPeople) {
        List<ConferenceRoom> availableRooms = conferenceRoomRepository.findByCapacityGreaterThanEqualOrderByCapacityAsc(numberOfPeople);

        for (ConferenceRoom room : availableRooms) {
            // Check for maintenance slots
            List<MaintenanceSlot> maintenanceSlots = maintenanceSlotRepository.findByRoomAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(room,
                    startTime.toLocalTime(), endTime.toLocalTime());

            // Check for overlapping meetings
            List<ScheduledMeeting> overlappingMeetings = scheduledMeetingRepository.findByConferenceRoomAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(room,
                    startTime, endTime);

            if (maintenanceSlots.isEmpty() && overlappingMeetings.isEmpty()) {
                return room;
            }
        }
        return null;
    }

    private boolean isMaintenanceSlot(LocalTime startTime, LocalTime endTime) {
        List<MaintenanceSlot> maintenanceSlots = maintenanceSlotRepository.findByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(startTime, endTime);
        return !maintenanceSlots.isEmpty();
    }

}
