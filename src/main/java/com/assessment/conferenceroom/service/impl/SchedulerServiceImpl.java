package com.assessment.conferenceroom.service.impl;

import com.assessment.conferenceroom.dto.BookingDTO;
import com.assessment.conferenceroom.dto.BookingResponseDto;
import com.assessment.conferenceroom.dto.ScheduledMeetingDto;
import com.assessment.conferenceroom.model.ConferenceRoom;
import com.assessment.conferenceroom.model.MaintenanceSlot;
import com.assessment.conferenceroom.model.ScheduledMeeting;
import com.assessment.conferenceroom.repo.ConferenceRoomRepository;
import com.assessment.conferenceroom.repo.MaintenanceSlotRepository;
import com.assessment.conferenceroom.repo.ScheduledMeetingRepository;
import com.assessment.conferenceroom.service.SchedulerService;
import com.assessment.conferenceroom.utility.CacheServiceSim;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
public class SchedulerServiceImpl implements SchedulerService {
    @Autowired
    MaintenanceSlotRepository maintenanceSlotRepository;
    @Autowired
    ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    ScheduledMeetingRepository scheduledMeetingRepository;

    @Autowired
    CacheServiceSim cacheServiceSim;

    @Override
    public BookingResponseDto checkAndCreateSchedule(BookingDTO bookingDTO) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        if (isMaintenanceSlot(bookingDTO.getStartDateTime().toLocalTime(), bookingDTO.getEndDateTime().toLocalTime())) {
            bookingResponseDto.setMessage("Cannot schedule a meeting during maintenance period.");
            bookingResponseDto.setRoomAvailable(false);
            return bookingResponseDto;
        }

        // Find available room and time slot
        ConferenceRoom conferenceRoom = findAvailableRoom(bookingDTO.getStartDateTime(), bookingDTO.getEndDateTime(), bookingDTO.getHeadcount(),bookingDTO.getIsCreate());
        if (conferenceRoom != null) {
            if (bookingDTO.getIsCreate()) {
                ScheduledMeeting meeting = new ScheduledMeeting();
                meeting.setStartTime(bookingDTO.getStartDateTime());
                meeting.setEndTime(bookingDTO.getEndDateTime());
                meeting.setHeadcount(bookingDTO.getHeadcount());

                // Assuming room_id is set based on availability
                meeting.setConferenceRoom(conferenceRoom);
                scheduledMeetingRepository.save(meeting);
                bookingResponseDto.setMessage("Conference room " +conferenceRoom.getConferenceRoomName()+ " scheduled successfully.");
            } else {
                ScheduledMeetingDto scheduledMeetingDto = new ScheduledMeetingDto();
                scheduledMeetingDto.setStartTime(bookingDTO.getStartDateTime());
                scheduledMeetingDto.setEndTime(bookingDTO.getEndDateTime());
                scheduledMeetingDto.setCachedTime(LocalDateTime.now().plusMinutes(5));
                scheduledMeetingDto.setConferenceRoomName(conferenceRoom.getConferenceRoomName());

                cacheServiceSim.updateData(conferenceRoom.getConferenceRoomName(), scheduledMeetingDto);
                bookingResponseDto.setMessage(conferenceRoom.getConferenceRoomName() + " is available book in next 60 sec");
            }
            bookingResponseDto.setRoomAvailable(true);
        } else {
            bookingResponseDto.setMessage("No available room or time slot found.");
            bookingResponseDto.setRoomAvailable(false);
        }
        return bookingResponseDto;
    }

    private ConferenceRoom findAvailableRoom(LocalDateTime startTime, LocalDateTime endTime, int numberOfPeople,boolean isCreate) {

        ScheduledMeetingDto scheduledMeetingDto = new ScheduledMeetingDto();
        scheduledMeetingDto.setStartTime(startTime);
        scheduledMeetingDto.setEndTime(endTime);
        scheduledMeetingDto.setCreate(isCreate);

        List<ConferenceRoom> availableRooms = conferenceRoomRepository.findByCapacityGreaterThanEqualOrderByCapacityAsc(numberOfPeople);

        for (ConferenceRoom room : availableRooms) {
            // Check for maintenance slots
            List<MaintenanceSlot> maintenanceSlots = maintenanceSlotRepository.findByRoomAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(room,
                    startTime.toLocalTime(), endTime.toLocalTime());

            // Check for overlapping meetings
            List<ScheduledMeeting> overlappingMeetings = scheduledMeetingRepository.findByConferenceRoomAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(room,
                    startTime, endTime);

            scheduledMeetingDto.setConferenceRoomName(room.getConferenceRoomName());

            if (maintenanceSlots.isEmpty() && overlappingMeetings.isEmpty() && !checkIsRoomDetailsCached(scheduledMeetingDto)) {
                return room;
            }
        }
        return null;
    }

    private boolean isMaintenanceSlot(LocalTime startTime, LocalTime endTime) {
        List<MaintenanceSlot> maintenanceSlots = maintenanceSlotRepository.findByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(startTime, endTime);
        return !maintenanceSlots.isEmpty();
    }

    private boolean checkIsRoomDetailsCached (ScheduledMeetingDto scheduledMeetingDto) {
        List<ScheduledMeetingDto> tempScheduledMeetings = cacheServiceSim.getData(scheduledMeetingDto.getConferenceRoomName());
        LocalDateTime currentTime = LocalDateTime.now();
        if (tempScheduledMeetings != null) {
            // Iterate through the cached meetings and check for overlaps
            for (ScheduledMeetingDto cachedMeeting : tempScheduledMeetings) {

                if (scheduledMeetingDto.isCreate() && currentTime.isBefore(cachedMeeting.getCachedTime())) {
                    return false;
                }else
                if (cachedMeeting.getCachedTime().isAfter(currentTime) && isOverlap(scheduledMeetingDto, cachedMeeting)) {
                    return true;
                }
            }
        }
        // No overlap found
        return false;
    }

    private boolean isOverlap(ScheduledMeetingDto meetingDto, ScheduledMeetingDto cachedMeeting) {
        // Check for overlap based on start and end times
        LocalDateTime meetingDtoStartTime = meetingDto.getStartTime();
        LocalDateTime meetingDtoEndTime = meetingDto.getEndTime();
        LocalDateTime cachedMeetingStartTime = cachedMeeting.getStartTime();
        LocalDateTime cachedMeetingEndTime = cachedMeeting.getEndTime();

        return meetingDtoStartTime.isBefore(cachedMeetingEndTime) &&
                meetingDtoEndTime.isAfter(cachedMeetingStartTime);
    }

}
