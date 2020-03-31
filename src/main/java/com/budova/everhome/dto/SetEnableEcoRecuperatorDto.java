package com.budova.everhome.dto;

import com.budova.everhome.domain.SetEnableEcoRecuperator;

import java.time.LocalDateTime;

public class SetEnableEcoRecuperatorDto {

    private LocalDateTime time;
    private Float value;

    public SetEnableEcoRecuperatorDto() {
    }

    public SetEnableEcoRecuperatorDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetEnableEcoRecuperatorDto(SetEnableEcoRecuperator setEnableEcoRecuperator) {
        this.time = setEnableEcoRecuperator.getTime();
        this.value = setEnableEcoRecuperator.getValue();
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
        return "SetEnableEcoRecuperatorDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
