package com.budova.everhome.domain;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(indexes = {
        @Index(name = "idx_parameter_id_time", columnList = "parameter_id,time")
})

public class Humidity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="parameter_id")
    private Parameter param;
    private LocalDateTime time;
    private Float value;

    public Humidity() {}

    public Humidity(Long id, Parameter param, LocalDateTime time, Float value) {
        this.id = id;
        this.param = param;
        this.time = time;
        this.value = value;
    }

    public Humidity(Parameter param, LocalDateTime time, Float value) {
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

    public static boolean isModuled(Humidity h1, Humidity h2, float module, int seconds){
        return Math.abs(h1.value - h2.value) > module || Duration.between(h1.time, h2.time).getSeconds() > seconds;
    }

    public static boolean isModuled(Humidity h1, Humidity h2){
        return isModuled(h1, h2, 0.5F, 60);
    }

    @Override
    public String toString(){
        return "Humidity{" +
                "id=" + id +
                ", param=" + param +
                ", time=" + time +
                ", value=" + value +
                '}';
    }
}
