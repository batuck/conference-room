package com.assessment.conferenceroom.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.assessment.conferenceroom.dto.BookingDTO;
import com.assessment.conferenceroom.dto.BookingResponseDto;
import com.assessment.conferenceroom.dto.ScheduledMeetingDto;
import com.assessment.conferenceroom.model.ConferenceRoom;
import com.assessment.conferenceroom.model.ScheduledMeeting;
import com.assessment.conferenceroom.repo.ConferenceRoomRepository;
import com.assessment.conferenceroom.repo.MaintenanceSlotRepository;
import com.assessment.conferenceroom.repo.ScheduledMeetingRepository;
import com.assessment.conferenceroom.utility.CacheServiceSim;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SchedulerServiceImpl.class})
@ExtendWith(SpringExtension.class)
class SchedulerServiceImplTest {
    @MockBean
    private CacheServiceSim cacheServiceSim;

    @MockBean
    private ConferenceRoomRepository conferenceRoomRepository;

    @MockBean
    private MaintenanceSlotRepository maintenanceSlotRepository;

    @MockBean
    private ScheduledMeetingRepository scheduledMeetingRepository;

    @Autowired
    private SchedulerServiceImpl schedulerServiceImpl;

    @Test
    void testCheckAndCreateSchedule() {
        // Arrange
        when(conferenceRoomRepository.findByCapacityGreaterThanEqualOrderByCapacityAsc(anyInt()))
                .thenReturn(new ArrayList<>());
        when(maintenanceSlotRepository.findOverlappingSlots(Mockito.<LocalTime>any(), Mockito.<LocalTime>any()))
                .thenReturn(new ArrayList<>());

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setEndDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        bookingDTO.setHeadcount(3);
        bookingDTO.setIsCreate(true);
        bookingDTO.setStartDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());

        // Act
        BookingResponseDto actualCheckAndCreateScheduleResult = schedulerServiceImpl.checkAndCreateSchedule(bookingDTO);

        // Assert
        verify(conferenceRoomRepository).findByCapacityGreaterThanEqualOrderByCapacityAsc(anyInt());
        verify(maintenanceSlotRepository).findOverlappingSlots(Mockito.<LocalTime>any(), Mockito.<LocalTime>any());
        assertEquals("No available room or time slot found.", actualCheckAndCreateScheduleResult.getMessage());
        assertFalse(actualCheckAndCreateScheduleResult.isRoomAvailable());
    }


    @Test
    void testCheckAndCreateSchedule2() {
        // Arrange
        when(cacheServiceSim.getData(Mockito.<String>any())).thenReturn(new ArrayList<>());

        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setConferenceRoomId(1L);

        ArrayList<ConferenceRoom> conferenceRoomList = new ArrayList<>();
        conferenceRoomList.add(conferenceRoom);
        when(conferenceRoomRepository.findByCapacityGreaterThanEqualOrderByCapacityAsc(anyInt()))
                .thenReturn(conferenceRoomList);
        when(maintenanceSlotRepository.findOverlappingSlots(Mockito.<LocalTime>any(), Mockito.<LocalTime>any()))
                .thenReturn(new ArrayList<>());

        ConferenceRoom conferenceRoom2 = new ConferenceRoom();
        conferenceRoom2.setConferenceRoomId(1L);

        ScheduledMeeting scheduledMeeting = new ScheduledMeeting();
        scheduledMeeting.setConferenceRoom(conferenceRoom2);
        scheduledMeeting.setEndTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        scheduledMeeting.setHeadcount(3);
        scheduledMeeting.setMeetingId(1L);
        scheduledMeeting.setStartTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(scheduledMeetingRepository.save(Mockito.<ScheduledMeeting>any())).thenReturn(scheduledMeeting);
        when(scheduledMeetingRepository.findByConferenceRoomAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                Mockito.<ConferenceRoom>any(), Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any()))
                .thenReturn(new ArrayList<>());

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setEndDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        bookingDTO.setHeadcount(3);
        bookingDTO.setIsCreate(true);
        bookingDTO.setStartDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());

        // Act
        BookingResponseDto actualCheckAndCreateScheduleResult = schedulerServiceImpl.checkAndCreateSchedule(bookingDTO);

        // Assert
        verify(conferenceRoomRepository).findByCapacityGreaterThanEqualOrderByCapacityAsc(anyInt());
        verify(maintenanceSlotRepository).findOverlappingSlots(Mockito.<LocalTime>any(), Mockito.<LocalTime>any());
        verify(scheduledMeetingRepository).findByConferenceRoomAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                Mockito.<ConferenceRoom>any(), Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any());
        verify(cacheServiceSim).getData(Mockito.<String>any());
        verify(scheduledMeetingRepository).save(Mockito.<ScheduledMeeting>any());
        assertEquals("Conference room null scheduled successfully.", actualCheckAndCreateScheduleResult.getMessage());
        assertTrue(actualCheckAndCreateScheduleResult.isRoomAvailable());
    }

    @Test
    void testCheckAndCreateSchedule3() {
        // Arrange
        ScheduledMeetingDto scheduledMeetingDto = new ScheduledMeetingDto();
        scheduledMeetingDto.setCachedTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        scheduledMeetingDto.setConferenceRoomName("Conference Room Name");
        scheduledMeetingDto.setCreate(true);
        scheduledMeetingDto.setEndTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        scheduledMeetingDto.setHeadcount(3);
        scheduledMeetingDto.setStartTime(LocalDate.of(1970, 1, 1).atStartOfDay());

        ArrayList<ScheduledMeetingDto> scheduledMeetingDtoList = new ArrayList<>();
        scheduledMeetingDtoList.add(scheduledMeetingDto);
        when(cacheServiceSim.getData(Mockito.<String>any())).thenReturn(scheduledMeetingDtoList);

        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setConferenceRoomId(1L);

        ArrayList<ConferenceRoom> conferenceRoomList = new ArrayList<>();
        conferenceRoomList.add(conferenceRoom);
        when(conferenceRoomRepository.findByCapacityGreaterThanEqualOrderByCapacityAsc(anyInt()))
                .thenReturn(conferenceRoomList);
        when(maintenanceSlotRepository.findOverlappingSlots(Mockito.<LocalTime>any(), Mockito.<LocalTime>any()))
                .thenReturn(new ArrayList<>());

        ConferenceRoom conferenceRoom2 = new ConferenceRoom();
        conferenceRoom2.setConferenceRoomId(1L);

        ScheduledMeeting scheduledMeeting = new ScheduledMeeting();
        scheduledMeeting.setConferenceRoom(conferenceRoom2);
        scheduledMeeting.setEndTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        scheduledMeeting.setHeadcount(3);
        scheduledMeeting.setMeetingId(1L);
        scheduledMeeting.setStartTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(scheduledMeetingRepository.save(Mockito.<ScheduledMeeting>any())).thenReturn(scheduledMeeting);
        when(scheduledMeetingRepository.findByConferenceRoomAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                Mockito.<ConferenceRoom>any(), Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any()))
                .thenReturn(new ArrayList<>());

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setEndDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        bookingDTO.setHeadcount(3);
        bookingDTO.setIsCreate(true);
        bookingDTO.setStartDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());

        // Act
        BookingResponseDto actualCheckAndCreateScheduleResult = schedulerServiceImpl.checkAndCreateSchedule(bookingDTO);

        // Assert
        verify(conferenceRoomRepository).findByCapacityGreaterThanEqualOrderByCapacityAsc(anyInt());
        verify(maintenanceSlotRepository).findOverlappingSlots(Mockito.<LocalTime>any(), Mockito.<LocalTime>any());
        verify(scheduledMeetingRepository).findByConferenceRoomAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                Mockito.<ConferenceRoom>any(), Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any());
        verify(cacheServiceSim).getData(Mockito.<String>any());
        verify(scheduledMeetingRepository).save(Mockito.<ScheduledMeeting>any());
        assertEquals("Conference room null scheduled successfully.", actualCheckAndCreateScheduleResult.getMessage());
        assertTrue(actualCheckAndCreateScheduleResult.isRoomAvailable());
    }

    @Test
    void testCheckAndCreateSchedule4() {
        // Arrange
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setConferenceRoomId(1L);

        ArrayList<ConferenceRoom> conferenceRoomList = new ArrayList<>();
        conferenceRoomList.add(conferenceRoom);
        when(conferenceRoomRepository.findByCapacityGreaterThanEqualOrderByCapacityAsc(anyInt()))
                .thenReturn(conferenceRoomList);
        when(maintenanceSlotRepository.findOverlappingSlots(Mockito.<LocalTime>any(), Mockito.<LocalTime>any()))
                .thenReturn(new ArrayList<>());

        ConferenceRoom conferenceRoom2 = new ConferenceRoom();
        conferenceRoom2.setConferenceRoomId(1L);

        ScheduledMeeting scheduledMeeting = new ScheduledMeeting();
        scheduledMeeting.setConferenceRoom(conferenceRoom2);
        scheduledMeeting.setEndTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        scheduledMeeting.setHeadcount(3);
        scheduledMeeting.setMeetingId(1L);
        scheduledMeeting.setStartTime(LocalDate.of(1970, 1, 1).atStartOfDay());

        ArrayList<ScheduledMeeting> scheduledMeetingList = new ArrayList<>();
        scheduledMeetingList.add(scheduledMeeting);
        when(scheduledMeetingRepository.findByConferenceRoomAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                Mockito.<ConferenceRoom>any(), Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any()))
                .thenReturn(scheduledMeetingList);

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setEndDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        bookingDTO.setHeadcount(3);
        bookingDTO.setIsCreate(true);
        bookingDTO.setStartDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());

        // Act
        BookingResponseDto actualCheckAndCreateScheduleResult = schedulerServiceImpl.checkAndCreateSchedule(bookingDTO);

        // Assert
        verify(conferenceRoomRepository).findByCapacityGreaterThanEqualOrderByCapacityAsc(anyInt());
        verify(maintenanceSlotRepository).findOverlappingSlots(Mockito.<LocalTime>any(), Mockito.<LocalTime>any());
        verify(scheduledMeetingRepository).findByConferenceRoomAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                Mockito.<ConferenceRoom>any(), Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any());
        assertEquals("No available room or time slot found.", actualCheckAndCreateScheduleResult.getMessage());
        assertFalse(actualCheckAndCreateScheduleResult.isRoomAvailable());
    }


    @Test
    void testCheckAndCreateSchedule5() {
        // Arrange
        doNothing().when(cacheServiceSim).updateData(Mockito.<String>any(), Mockito.<ScheduledMeetingDto>any());
        when(cacheServiceSim.getData(Mockito.<String>any())).thenReturn(new ArrayList<>());

        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setConferenceRoomId(1L);

        ArrayList<ConferenceRoom> conferenceRoomList = new ArrayList<>();
        conferenceRoomList.add(conferenceRoom);
        when(conferenceRoomRepository.findByCapacityGreaterThanEqualOrderByCapacityAsc(anyInt()))
                .thenReturn(conferenceRoomList);
        when(maintenanceSlotRepository.findOverlappingSlots(Mockito.<LocalTime>any(), Mockito.<LocalTime>any()))
                .thenReturn(new ArrayList<>());
        when(scheduledMeetingRepository.findByConferenceRoomAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                Mockito.<ConferenceRoom>any(), Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any()))
                .thenReturn(new ArrayList<>());
        BookingDTO bookingDTO = mock(BookingDTO.class);
        when(bookingDTO.getIsCreate()).thenReturn(false);
        when(bookingDTO.getHeadcount()).thenReturn(3);
        when(bookingDTO.getEndDateTime()).thenReturn(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(bookingDTO.getStartDateTime()).thenReturn(LocalDate.of(1970, 1, 1).atStartOfDay());
        doNothing().when(bookingDTO).setEndDateTime(Mockito.<LocalDateTime>any());
        doNothing().when(bookingDTO).setHeadcount(Mockito.<Integer>any());
        doNothing().when(bookingDTO).setIsCreate(Mockito.<Boolean>any());
        doNothing().when(bookingDTO).setStartDateTime(Mockito.<LocalDateTime>any());
        bookingDTO.setEndDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        bookingDTO.setHeadcount(3);
        bookingDTO.setIsCreate(true);
        bookingDTO.setStartDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());

        // Act
        BookingResponseDto actualCheckAndCreateScheduleResult = schedulerServiceImpl.checkAndCreateSchedule(bookingDTO);

        // Assert
        verify(bookingDTO, atLeast(1)).getEndDateTime();
        verify(bookingDTO).getHeadcount();
        verify(bookingDTO, atLeast(1)).getIsCreate();
        verify(bookingDTO, atLeast(1)).getStartDateTime();
        verify(bookingDTO).setEndDateTime(Mockito.<LocalDateTime>any());
        verify(bookingDTO).setHeadcount(Mockito.<Integer>any());
        verify(bookingDTO).setIsCreate(Mockito.<Boolean>any());
        verify(bookingDTO).setStartDateTime(Mockito.<LocalDateTime>any());
        verify(conferenceRoomRepository).findByCapacityGreaterThanEqualOrderByCapacityAsc(anyInt());
        verify(maintenanceSlotRepository).findOverlappingSlots(Mockito.<LocalTime>any(), Mockito.<LocalTime>any());
        verify(scheduledMeetingRepository).findByConferenceRoomAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                Mockito.<ConferenceRoom>any(), Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any());
        verify(cacheServiceSim).getData(Mockito.<String>any());
        verify(cacheServiceSim).updateData(Mockito.<String>any(), Mockito.<ScheduledMeetingDto>any());
        assertEquals("null is available book in next 60 sec", actualCheckAndCreateScheduleResult.getMessage());
        assertTrue(actualCheckAndCreateScheduleResult.isRoomAvailable());
    }

    @Test
    void testCheckAndCreateSchedule6() {
        // Arrange
        ScheduledMeetingDto scheduledMeetingDto = new ScheduledMeetingDto();
        scheduledMeetingDto.setCachedTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        scheduledMeetingDto.setConferenceRoomName("Conference Room Name");
        scheduledMeetingDto.setCreate(false);
        scheduledMeetingDto.setEndTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        scheduledMeetingDto.setHeadcount(3);
        scheduledMeetingDto.setStartTime(LocalDate.of(1970, 1, 1).atStartOfDay());

        ArrayList<ScheduledMeetingDto> scheduledMeetingDtoList = new ArrayList<>();
        scheduledMeetingDtoList.add(scheduledMeetingDto);
        doNothing().when(cacheServiceSim).updateData(Mockito.<String>any(), Mockito.<ScheduledMeetingDto>any());
        when(cacheServiceSim.getData(Mockito.<String>any())).thenReturn(scheduledMeetingDtoList);

        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setConferenceRoomId(1L);

        ArrayList<ConferenceRoom> conferenceRoomList = new ArrayList<>();
        conferenceRoomList.add(conferenceRoom);
        when(conferenceRoomRepository.findByCapacityGreaterThanEqualOrderByCapacityAsc(anyInt()))
                .thenReturn(conferenceRoomList);
        when(maintenanceSlotRepository.findOverlappingSlots(Mockito.<LocalTime>any(), Mockito.<LocalTime>any()))
                .thenReturn(new ArrayList<>());
        when(scheduledMeetingRepository.findByConferenceRoomAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                Mockito.<ConferenceRoom>any(), Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any()))
                .thenReturn(new ArrayList<>());
        BookingDTO bookingDTO = mock(BookingDTO.class);
        when(bookingDTO.getIsCreate()).thenReturn(false);
        when(bookingDTO.getHeadcount()).thenReturn(3);
        when(bookingDTO.getEndDateTime()).thenReturn(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(bookingDTO.getStartDateTime()).thenReturn(LocalDate.of(1970, 1, 1).atStartOfDay());
        doNothing().when(bookingDTO).setEndDateTime(Mockito.<LocalDateTime>any());
        doNothing().when(bookingDTO).setHeadcount(Mockito.<Integer>any());
        doNothing().when(bookingDTO).setIsCreate(Mockito.<Boolean>any());
        doNothing().when(bookingDTO).setStartDateTime(Mockito.<LocalDateTime>any());
        bookingDTO.setEndDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        bookingDTO.setHeadcount(3);
        bookingDTO.setIsCreate(true);
        bookingDTO.setStartDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());

        // Act
        BookingResponseDto actualCheckAndCreateScheduleResult = schedulerServiceImpl.checkAndCreateSchedule(bookingDTO);

        // Assert
        verify(bookingDTO, atLeast(1)).getEndDateTime();
        verify(bookingDTO).getHeadcount();
        verify(bookingDTO, atLeast(1)).getIsCreate();
        verify(bookingDTO, atLeast(1)).getStartDateTime();
        verify(bookingDTO).setEndDateTime(Mockito.<LocalDateTime>any());
        verify(bookingDTO).setHeadcount(Mockito.<Integer>any());
        verify(bookingDTO).setIsCreate(Mockito.<Boolean>any());
        verify(bookingDTO).setStartDateTime(Mockito.<LocalDateTime>any());
        verify(conferenceRoomRepository).findByCapacityGreaterThanEqualOrderByCapacityAsc(anyInt());
        verify(maintenanceSlotRepository).findOverlappingSlots(Mockito.<LocalTime>any(), Mockito.<LocalTime>any());
        verify(scheduledMeetingRepository).findByConferenceRoomAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                Mockito.<ConferenceRoom>any(), Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any());
        verify(cacheServiceSim).getData(Mockito.<String>any());
        verify(cacheServiceSim).updateData(Mockito.<String>any(), Mockito.<ScheduledMeetingDto>any());
        assertEquals("null is available book in next 60 sec", actualCheckAndCreateScheduleResult.getMessage());
        assertTrue(actualCheckAndCreateScheduleResult.isRoomAvailable());
    }
}
