package com.budova.everhome.dto;

import com.budova.everhome.domain.SetCustom;

import java.time.LocalDateTime;

public class SetCustomDto {

    private LocalDateTime time;
    private Float value;

    public SetCustomDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetCustomDto(SetCustom setCustom){
        this.time = setCustom.getTime();
        this.value = setCustom.getValue();
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
        return "SetCustomDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
