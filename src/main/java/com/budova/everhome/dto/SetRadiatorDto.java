package com.budova.everhome.dto;

import com.budova.everhome.domain.SetRadiator;

import java.time.LocalDateTime;

public class SetRadiatorDto {

    private LocalDateTime time;
    private Float value;

    public SetRadiatorDto(){

    }

    public SetRadiatorDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetRadiatorDto(SetRadiator setRadiator){
        this.time = setRadiator.getTime();
        this.value = setRadiator.getValue();
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
        return "SetRadiator{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
