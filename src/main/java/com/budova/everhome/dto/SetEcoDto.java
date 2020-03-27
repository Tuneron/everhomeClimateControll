package com.budova.everhome.dto;

import com.budova.everhome.domain.SetEco;

import java.time.LocalDateTime;

public class SetEcoDto {

    private LocalDateTime time;
    private Float value;

    public SetEcoDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetEcoDto(SetEco setEco){
        this.time = setEco.getTime();
        this.value = setEco.getValue();
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
        return "SetEcoDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
