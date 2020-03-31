package com.budova.everhome.dto;

import com.budova.everhome.domain.SetNight;

import java.time.LocalDateTime;

public class SetNightDto {

    private LocalDateTime time;
    private Float value;

    public SetNightDto() {
    }

    public SetNightDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetNightDto(SetNight setNight) {
        this.time = setNight.getTime();
        this.value = setNight.getValue();
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SetNightDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
