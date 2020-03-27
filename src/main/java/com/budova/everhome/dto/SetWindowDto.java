package com.budova.everhome.dto;

import com.budova.everhome.domain.SetWindow;

import java.time.LocalDateTime;

public class SetWindowDto {

    private LocalDateTime time;
    private Float value;

    public SetWindowDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetWindowDto(SetWindow setWindow) {
        this.time = setWindow.getTime();
        this.value = setWindow.getValue();
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
        return "SetWindowDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
