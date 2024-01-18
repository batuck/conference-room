package com.assessment.conferenceroom.utility;

import com.assessment.conferenceroom.dto.ScheduledMeetingDto;
import com.assessment.conferenceroom.model.ConferenceRoom;
import com.assessment.conferenceroom.model.ScheduledMeeting;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheServiceSim {

    private final Map<String, List<ScheduledMeetingDto>> dataStore = new ConcurrentHashMap<>();

    @Cacheable("conferenceCache")
    public List<ScheduledMeetingDto> getData(String key) {
        // Simulate data retrieval (this will be cached)
        System.out.println("Fetching data from the actual source...");
        return dataStore.get(key);
    }

    @CachePut("conferenceCache")
    public void updateData(String key, ScheduledMeetingDto newData) {
        // Simulate data update
        System.out.println("Updating data in the actual source...");
        List<ScheduledMeetingDto> scheduledMeetingDtoList = new ArrayList<>();
        if(CollectionUtils.isEmpty(dataStore.get(key))){
            scheduledMeetingDtoList.add(newData);
        }else{
            scheduledMeetingDtoList = dataStore.get(key);
            scheduledMeetingDtoList.add(newData);
        }
        // Save the updated data to the concurrent in-memory store
        dataStore.put(key, scheduledMeetingDtoList);

    }

}
