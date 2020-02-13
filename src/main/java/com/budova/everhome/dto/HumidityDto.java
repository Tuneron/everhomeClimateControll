package com.budova.everhome.dto;

import com.budova.everhome.domain.Humidity;

import java.time.LocalDateTime;

public class HumidityDto {

    private LocalDateTime time;
    private Float value;

    public HumidityDto() {}

    public HumidityDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public HumidityDto(Humidity h) {
        this.time = h.getTime();
        this.value = h.getValue();
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
}
