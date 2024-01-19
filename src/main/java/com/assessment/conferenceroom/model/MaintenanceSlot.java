package com.assessment.conferenceroom.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@ToString
@NoArgsConstructor(force = true)
public class MaintenanceSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maintenanceSlotId;
    private final LocalTime startTime;
    private final LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "conference_room_id")
    private final ConferenceRoom room;


}
