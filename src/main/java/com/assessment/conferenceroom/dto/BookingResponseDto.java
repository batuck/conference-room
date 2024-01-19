package com.assessment.conferenceroom.dto;

import lombok.Data;

@Data
public class BookingResponseDto {

    private boolean roomAvailable;
    private String message;
}
