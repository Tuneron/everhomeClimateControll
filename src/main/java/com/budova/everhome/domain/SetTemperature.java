package com.budova.everhome.domain;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(indexes = {
        @Index(name = "idx_parameter_id_time", columnList = "parameter_id,time")
})

public class SetTemperature {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "parameter_id")
    private Parameter param;
    private LocalDateTime time;
    private Float value;
    private static final float MIN_VALUE = 16F;
    private static final float MAX_VALUE = 31F;

    public SetTemperature() { }

    public SetTemperature(Long id, Parameter param, LocalDateTime time, Float value) {
        this.id = id;
        this.param = param;
        this.time = time;
        this.value = value;
    }

    public SetTemperature(Parameter param, LocalDateTime time, Float value) {
        this.param = param;
        this.time = time;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Parameter getParam() {
        return param;
    }

    public void setParam(Parameter param) {
        this.param = param;
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

    public static boolean isModuled(SetTemperature v1, SetTemperature v2, float module, int seconds) {
        return Math.abs(v1.value - v2.value) > module || Duration.between(v1.time, v2.time).getSeconds() > seconds;
    }

    public static boolean isModuled(SetTemperature v1, SetTemperature v2) {
        return isModuled(v1, v2, 0.5F, 300);
    }

    public static float inBorder(float value){
        if (value < MIN_VALUE) return MIN_VALUE;
        else
            if(value > MAX_VALUE) return MAX_VALUE;
            else
                return value;
    }

    public static float inBorder(SetTemperature value){
        if (value.getValue() < MIN_VALUE) return MIN_VALUE;
        else
        if(value.getValue() > MAX_VALUE) return MAX_VALUE;
        else
            return value.getValue();
    }

    @Override
    public String toString() {
        return "SetTemperature{" +
                "id=" + id +
                ", param=" + param +
                ", time=" + time +
                ", value=" + value +
                '}';
    }
}
