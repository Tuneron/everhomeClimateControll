package com.budova.everhome.dto;

import com.budova.everhome.domain.SetEnableEcoConditioner;

import java.time.LocalDateTime;

public class SetEnableEcoConditionerDto {

    private LocalDateTime time;
    private Float value;

    public SetEnableEcoConditionerDto() {
    }

    public SetEnableEcoConditionerDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetEnableEcoConditionerDto(SetEnableEcoConditioner setEnableEcoConditioner) {
        this.time = setEnableEcoConditioner.getTime();
        this.value = setEnableEcoConditioner.getValue();
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
        return "SetEnableEcoConditionerDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }

}
