package com.budova.everhome.dto;

import com.budova.everhome.domain.SetRecuperator;

import java.time.LocalDateTime;

public class SetRecuperatorDto {

    private LocalDateTime time;
    private Float value;

    public SetRecuperatorDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetRecuperatorDto(SetRecuperator setRecuperator){
        this.time = setRecuperator.getTime();
        this.value = setRecuperator.getValue();
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
        return "SetRecuperatorDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
