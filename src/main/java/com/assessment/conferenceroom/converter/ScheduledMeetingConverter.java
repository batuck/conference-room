package com.assessment.conferenceroom.converter;

import com.assessment.conferenceroom.dto.BookingDTO;
import com.assessment.conferenceroom.dto.ScheduledMeetingDto;
import com.assessment.conferenceroom.model.ScheduledMeeting;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

public class ScheduledMeetingConverter implements Converter<ScheduledMeetingDto, ScheduledMeeting> {


    @Override
    public ScheduledMeeting convert(ScheduledMeetingDto scheduledMeetingDto) {
        ScheduledMeeting scheduledMeeting = new ScheduledMeeting();
        BeanUtils.copyProperties(scheduledMeetingDto,scheduledMeeting);
        return null;
    }
}
