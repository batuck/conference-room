package com.assessment.conferenceroom.controller;

import com.assessment.conferenceroom.dto.BookingDTO;
import com.assessment.conferenceroom.dto.BookingResponseDto;
import com.assessment.conferenceroom.service.SchedulerService;
import com.assessment.conferenceroom.utility.BookingDtoValidator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@Validated
@RequestMapping(value = "/bookings",produces = MediaType.APPLICATION_JSON_VALUE)
public class SchedulerController {

    @Autowired
    SchedulerService schedulerService;
    @PostMapping(value = "/check")
    public ResponseEntity<BookingResponseDto> checkSchedule(@Valid @RequestBody BookingDTO bookingDTO){
        log.info("Received input check {}",bookingDTO);
        BookingResponseDto response = schedulerService.checkAndCreateSchedule(bookingDTO);
        log.info("Generated response {}",response);
        return ResponseEntity.ok(response);
    }


    @InitBinder("bookingDTO")
    protected void initBookingDtoBinder(WebDataBinder binder){
        binder.setValidator(new BookingDtoValidator());
    }
}
