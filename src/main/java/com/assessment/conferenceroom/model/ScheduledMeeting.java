package com.assessment.conferenceroom.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
public class ScheduledMeeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long meetingId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer headcount;

    @ManyToOne
    @JoinColumn(name = "conference_room_id")
    private ConferenceRoom conferenceRoom;



}
