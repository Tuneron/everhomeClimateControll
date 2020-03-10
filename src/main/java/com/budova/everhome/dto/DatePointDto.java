package com.budova.everhome.dto;

import com.budova.everhome.domain.DatePoint;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DatePointDto {

    private LocalDateTime time;
    private LocalDateTime date;

    public DatePointDto() {
    }

    public DatePointDto(LocalDateTime time, LocalDateTime value) {
        this.time = time;
        this.date = value;
    }

    public DatePointDto(DatePoint datePoint){
        this.date = datePoint.getDate();
        this.time = datePoint.getTime();
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public LocalDateTime getValue() {
        return date;
    }

    public void setValue(LocalDateTime value) {
        this.date = value;
    }

    @Override
    public String toString() {
        return "DatePointDto{" +
                "time=" + time +
                ", time=" + time +
                ", date=" + date +
                '}';
    }
}
