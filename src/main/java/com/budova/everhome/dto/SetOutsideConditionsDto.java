package com.budova.everhome.dto;

import com.budova.everhome.domain.SetOutsideConditions;

import java.time.LocalDateTime;

public class SetOutsideConditionsDto {

    private LocalDateTime time;
    private Float value;

    public SetOutsideConditionsDto() {
    }

    public SetOutsideConditionsDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetOutsideConditionsDto(SetOutsideConditions setOutsideConditions) {
        this.time = setOutsideConditions.getTime();
        this.value = setOutsideConditions.getValue();
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
        return "SetOutsideConditionsDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
