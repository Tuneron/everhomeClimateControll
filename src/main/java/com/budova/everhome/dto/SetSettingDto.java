package com.budova.everhome.dto;

import com.budova.everhome.domain.SetSetting;

import java.time.LocalDateTime;

public class SetSettingDto {

    private LocalDateTime time;
    private Float value;

    public SetSettingDto() {
    }

    public SetSettingDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetSettingDto(SetSetting setSetting){
        this.time = setSetting.getTime();
        this.value = setSetting.getValue();
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
        return "SetSettingDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
