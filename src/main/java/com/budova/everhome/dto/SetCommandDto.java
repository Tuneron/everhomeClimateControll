package com.budova.everhome.dto;

import com.budova.everhome.domain.SetCommand;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SetCommandDto {

    private LocalDateTime time;
    private Float value;

    public SetCommandDto() {
    }

    public SetCommandDto(LocalDateTime time, Float value) {
        this.time = time;
        this.value = value;
    }

    public SetCommandDto(SetCommand setCommand){
        this.time = setCommand.getTime();
        this.value = setCommand.getValue();
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
        return "SetCommandDto{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
