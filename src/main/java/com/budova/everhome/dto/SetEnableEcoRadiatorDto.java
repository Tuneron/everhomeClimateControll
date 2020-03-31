package com.budova.everhome.dto;

import com.budova.everhome.domain.SetEnableEcoRadiator;

import java.time.LocalDateTime;

public class SetEnableEcoRadiatorDto {

    private LocalDateTime time;
    private Float value;

    public SetEnableEcoRadiatorDto() {
    }

    public SetEnableEcoRadiatorDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetEnableEcoRadiatorDto(SetEnableEcoRadiator setEnableEcoRadiator) {
        this.time = setEnableEcoRadiator.getTime();
        this.value = setEnableEcoRadiator.getValue();
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
        return "SetEnableEcoRadiatorDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }

}
