package com.assessment.conferenceroom.utility;

import com.assessment.conferenceroom.dto.BookingDTO;

import lombok.NonNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class BookingDtoValidator implements Validator {
    @Override
    public void validate(@NonNull Object target, Errors errors) {
        BookingDTO bookingDTO = (BookingDTO) target;
        LocalDateTime startTime = bookingDTO.getStartDateTime();
        LocalDateTime endTime = bookingDTO.getEndDateTime();

        /*if(null == startTime ){
            errors.rejectValue("startDateTime" , "Please enter start time");
        }

        if(null == endTime){
            errors.rejectValue("endDateTime" , "Please enter end time");
        }

        if(null != endTime && endTime.isBefore(LocalDateTime.now())){
            errors.rejectValue("endDateTime" , "End time is prior current time");
        }

        if(null != startTime && startTime.isBefore(LocalDateTime.now())){
            errors.rejectValue("startDateTime" , "Start time is prior current time");
        }


        if(null != endTime && endTime.isBefore(startTime)){
            errors.rejectValue("endDateTime" , "End time is prior to start time");
        }

        assert startTime != null;
        assert endTime != null;
        long durationInMinutes = ChronoUnit.MINUTES.between(startTime.toLocalTime(), endTime.toLocalTime());
        if(durationInMinutes % 15 != 0){
            errors.rejectValue("endDateTime" , "Selected time is not in multiple of 15 minutes");
        }*/
    }



    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return BookingDTO.class.equals(clazz);
    }

}
