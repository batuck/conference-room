package com.assessment.conferenceroom.controller;

import com.assessment.conferenceroom.dto.BookingDTO;
import com.assessment.conferenceroom.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/bookings",produces = MediaType.APPLICATION_JSON_VALUE)
public class SchedulerController {

    @Autowired
    SchedulerService schedulerService;
    @PostMapping(value = "/check")
    public ResponseEntity<String> checkSchedule(@RequestBody BookingDTO bookingDTO){
        log.info("Received input check {}",bookingDTO);
        String response = schedulerService.checkAndCreateSchedule(bookingDTO);
        return ResponseEntity.ok("Success "+response);
    }
}
