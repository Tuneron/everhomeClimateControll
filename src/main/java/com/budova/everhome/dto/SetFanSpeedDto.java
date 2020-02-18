package com.budova.everhome.dto;

import com.budova.everhome.domain.SetFanSpeed;

import java.time.LocalDateTime;

public class SetFanSpeedDto {

    private LocalDateTime time;
    private Float value;

    public SetFanSpeedDto(){
    }

    public SetFanSpeedDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public  SetFanSpeedDto(SetFanSpeed setFanSpeed)
    {
        this.time = setFanSpeed.getTime();
        this.value = setFanSpeed.getValue();
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
        return "SetFanSpeedDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
