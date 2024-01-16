package com.assessment.conferenceroom.controller;

import com.assessment.conferenceroom.dto.BookingDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/bookings",produces = MediaType.APPLICATION_JSON_VALUE)
public class SchedulerController {
    @PostMapping(value = "/check")
    public ResponseEntity<String> checkSchedule(@RequestBody BookingDTO bookingDTO){
        log.info("Received input check {}",bookingDTO);
        log.info("Month {}",bookingDTO.getEndDateTime().getMonth());
        return ResponseEntity.ok("Success");
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createSchedule(@RequestBody BookingDTO bookingDTO){
        log.info("Received input create {}",bookingDTO);
        return ResponseEntity.ok("Success");
    }
}
