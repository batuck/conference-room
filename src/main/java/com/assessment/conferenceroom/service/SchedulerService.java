package com.assessment.conferenceroom.service;

import com.assessment.conferenceroom.dto.BookingDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public interface SchedulerService {

    String checkAndCreateSchedule(@RequestBody BookingDTO bookingDTO);

}
