package com.budova.everhome.dto;

import com.budova.everhome.domain.SetHumidifier;

import java.time.LocalDateTime;

public class SetHumidifierDto {

    private LocalDateTime time;
    private Float value;

    public SetHumidifierDto(){}

    public SetHumidifierDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetHumidifierDto(SetHumidifier setHumidifier){
        this.time = setHumidifier.getTime();
        this.value = setHumidifier.getValue();
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
        return "SetHumidifierDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
