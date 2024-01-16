package com.assessment.conferenceroom.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDTO {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer headcount;
}
