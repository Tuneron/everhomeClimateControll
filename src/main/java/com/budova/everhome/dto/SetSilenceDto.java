package com.budova.everhome.dto;

import com.budova.everhome.domain.SetSilence;

import java.time.LocalDateTime;

public class SetSilenceDto {

    private LocalDateTime time;
    private Float value;

    public SetSilenceDto() {
    }

    public SetSilenceDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetSilenceDto(SetSilence setSilence) {
        this.time = setSilence.getTime();
        this.value = setSilence.getValue();
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
        return "SetSilenceDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
