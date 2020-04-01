package com.budova.everhome.dto;

import com.budova.everhome.domain.SetEnableEcoWaterFloor;

import java.time.LocalDateTime;

public class SetEnableEcoWaterFloorDto {

    private LocalDateTime time;
    private Float value;

    public SetEnableEcoWaterFloorDto() {
    }

    public SetEnableEcoWaterFloorDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetEnableEcoWaterFloorDto(SetEnableEcoWaterFloor setEnableEcoWaterFloor) {
        this.time = setEnableEcoWaterFloor.getTime();
        this.value = setEnableEcoWaterFloor.getValue();
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
        return "SetEnableEcoWaterFloorDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
