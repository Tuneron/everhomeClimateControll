package com.budova.everhome.dto;

import com.budova.everhome.domain.SetElectricFloor;

import java.time.LocalDateTime;

public class SetElectricFloorDto {

    private LocalDateTime time;
    private Float value;

    public SetElectricFloorDto() {
    }

    public SetElectricFloorDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetElectricFloorDto(SetElectricFloor setElectricFloor) {
        this.time = setElectricFloor.getTime();
        this.value = setElectricFloor.getValue();
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
        return "SetElectricFloorDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
