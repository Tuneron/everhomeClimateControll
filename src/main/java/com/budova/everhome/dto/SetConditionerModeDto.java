package com.budova.everhome.dto;

import com.budova.everhome.domain.SetConditionerMode;

import java.time.LocalDateTime;

public class SetConditionerModeDto {

    private LocalDateTime time;
    private Float value;

    public SetConditionerModeDto() {
    }

    public SetConditionerModeDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public  SetConditionerModeDto(SetConditionerMode setConditionerMode){
        this.time = setConditionerMode.getTime();
        this.value = setConditionerMode.getValue();
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
