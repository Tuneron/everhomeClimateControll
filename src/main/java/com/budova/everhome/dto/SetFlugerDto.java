package com.budova.everhome.dto;

import com.budova.everhome.domain.SetFluger;

import java.time.LocalDateTime;

public class SetFlugerDto {

    private LocalDateTime time;
    private Float value;

    public SetFlugerDto(){}

    public SetFlugerDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetFlugerDto(SetFluger setFluger){
        this.time = setFluger.getTime();
        this.value = setFluger.getValue();
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
        return "SetFlugerDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
