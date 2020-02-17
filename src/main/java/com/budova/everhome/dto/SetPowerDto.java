package com.budova.everhome.dto;

import com.budova.everhome.domain.SetPower;

import java.time.LocalDateTime;

public class SetPowerDto {

    private LocalDateTime time;
    private Float value;

    public SetPowerDto() {
    }

    public SetPowerDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetPowerDto(SetPower setPower){
        this.time = setPower.getTime();
        this.value = setPower.getValue();
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
