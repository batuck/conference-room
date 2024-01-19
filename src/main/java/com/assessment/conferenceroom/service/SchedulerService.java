package com.assessment.conferenceroom.service;

import com.assessment.conferenceroom.dto.BookingDTO;
import com.assessment.conferenceroom.dto.BookingResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public interface SchedulerService {

    BookingResponseDto checkAndCreateSchedule(@RequestBody BookingDTO bookingDTO);

}
