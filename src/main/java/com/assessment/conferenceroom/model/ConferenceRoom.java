package com.assessment.conferenceroom.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ConferenceRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conference_room_id")
    private Long conferenceRoomId;
    private String conferenceRoomName;
    private Integer capacity;

}
