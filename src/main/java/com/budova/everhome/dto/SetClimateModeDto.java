package com.budova.everhome.dto;

import com.budova.everhome.domain.SetClimateMode;

import java.time.LocalDateTime;

public class SetClimateModeDto {

    private LocalDateTime time;
    private Float value;

    public SetClimateModeDto() {
    }

    public SetClimateModeDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetClimateModeDto(SetClimateMode setClimateMode){
        this.time = setClimateMode.getTime();
        this.value = setClimateMode.getValue();
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
        return "SetClimateModeDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
