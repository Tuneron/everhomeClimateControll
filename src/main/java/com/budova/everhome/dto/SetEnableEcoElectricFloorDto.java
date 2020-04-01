package com.budova.everhome.dto;

import com.budova.everhome.domain.SetEnableEcoElectricFloor;

import java.time.LocalDateTime;

public class SetEnableEcoElectricFloorDto {

    private LocalDateTime time;
    private Float value;

    public SetEnableEcoElectricFloorDto() {
    }

    public SetEnableEcoElectricFloorDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetEnableEcoElectricFloorDto(SetEnableEcoElectricFloor setEnableEcoElectricFloor) {
        this.time = setEnableEcoElectricFloor.getTime();
        this.value = setEnableEcoElectricFloor.getValue();
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
        return "SetEnableEcoElectricFloorDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
