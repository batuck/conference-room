package com.assessment.conferenceroom.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ConferenceRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conference_room_id")
    private Long conferenceRoomId;
    private final String conferenceRoomName;
    private final Integer capacity;


}
