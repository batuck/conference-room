package com.assessment.conferenceroom.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Entity
public class MaintenanceSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maintenanceSlotId;
    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "conference_room_id")
    private ConferenceRoom room;


}
