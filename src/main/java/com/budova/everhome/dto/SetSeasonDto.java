package com.budova.everhome.dto;

import com.budova.everhome.domain.SetSeason;

import java.time.LocalDateTime;

public class SetSeasonDto {

    private LocalDateTime time;
    private Float value;

    public SetSeasonDto() {
    }

    public SetSeasonDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetSeasonDto(SetSeason setSeason){
        this.time = setSeason.getTime();
        this.value = setSeason.getValue();
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
        return "SetSeasonDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }

}
