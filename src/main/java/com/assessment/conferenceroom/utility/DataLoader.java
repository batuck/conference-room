package com.assessment.conferenceroom.utility;


import com.assessment.conferenceroom.model.ConferenceRoom;
import com.assessment.conferenceroom.model.MaintenanceSlot;
import com.assessment.conferenceroom.repo.ConferenceRoomRepository;
import com.assessment.conferenceroom.repo.MaintenanceSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    private MaintenanceSlotRepository maintenanceSlotRepository;


    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        /*if (conferenceRoomRepository.count() == 0) {
            prePopulateConferenceRoomData();
        }*/

        if (maintenanceSlotRepository.count() == 0) {
            prePopulateData();
        }
    }

    private void prePopulateData() {
        ConferenceRoom room1 = new ConferenceRoom("Amaze", 3);
        room1 = conferenceRoomRepository.save(room1);
        ConferenceRoom room2 = new ConferenceRoom("Beauty", 7);
        room2 = conferenceRoomRepository.save(room2);
        ConferenceRoom room3 = new ConferenceRoom("Inspire", 12);
        room3 = conferenceRoomRepository.save(room3);
        ConferenceRoom room4 = new ConferenceRoom("Strive", 20);
        room4 = conferenceRoomRepository.save(room4);

        MaintenanceSlot slot1 = new MaintenanceSlot(LocalTime.of(9,0), LocalTime.of(9,15), room1);
        MaintenanceSlot slot2 = new MaintenanceSlot(LocalTime.of(13,0), LocalTime.of(13,15), room1);
        MaintenanceSlot slot3 = new MaintenanceSlot(LocalTime.of(17,0), LocalTime.of(17,15), room1);

        MaintenanceSlot slot4 = new MaintenanceSlot(LocalTime.of(9,0), LocalTime.of(9,15), room2);
        MaintenanceSlot slot5 = new MaintenanceSlot(LocalTime.of(13,0), LocalTime.of(13,15), room2);
        MaintenanceSlot slot6 = new MaintenanceSlot(LocalTime.of(17,0), LocalTime.of(17,15), room2);

        MaintenanceSlot slot7 = new MaintenanceSlot(LocalTime.of(9,0), LocalTime.of(9,15), room3);
        MaintenanceSlot slot8 = new MaintenanceSlot(LocalTime.of(13,0), LocalTime.of(13,15), room3);
        MaintenanceSlot slot9 = new MaintenanceSlot(LocalTime.of(17,0), LocalTime.of(17,15), room3);

        MaintenanceSlot slot10 = new MaintenanceSlot(LocalTime.of(9,0), LocalTime.of(9,15), room4);
        MaintenanceSlot slot11 = new MaintenanceSlot(LocalTime.of(13,0), LocalTime.of(13,15), room4);
        MaintenanceSlot slot12 = new MaintenanceSlot(LocalTime.of(17,0), LocalTime.of(17,15), room4);

        maintenanceSlotRepository.saveAll(List.of(slot1,slot2,slot3,slot4,slot5,slot6,slot7,slot8,slot9,slot10,slot11,slot12));
    }

    /*private void prePopulateConferenceRoomData() {
        ConferenceRoom room1 = new ConferenceRoom("Amaze", 3);
        ConferenceRoom room2 = new ConferenceRoom("Beauty", 7);
        ConferenceRoom room3 = new ConferenceRoom("Inspire", 12);
        ConferenceRoom room4 = new ConferenceRoom("Strive", 20);
        // Save the entities to the repository
        conferenceRoomRepository.saveAll(List.of(room1, room2, room3,room4));
    }*/
}

