package com.budova.everhome.dto;

import com.budova.everhome.domain.SetWaterFloor;

import java.time.LocalDateTime;

public class SetWaterFloorDto {

    private LocalDateTime time;
    private Float value;

    public SetWaterFloorDto() {
    }

    public SetWaterFloorDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetWaterFloorDto(SetWaterFloor setWaterFloor){
        this.time = setWaterFloor.getTime();
        this.value = setWaterFloor.getValue();
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
        return "SetWaterFloorDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
