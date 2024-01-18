package com.assessment.conferenceroom.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduledMeetingDto {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer headcount;
    private LocalDateTime cachedTime;
    private String conferenceRoomName;

}
