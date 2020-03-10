package com.budova.everhome.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Table(indexes = {
        @Index(name = "idx_parameter_id_time", columnList = "parameter_id,time")
})

public class DatePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "parameter_id")
    private Parameter param;
    private LocalDateTime time;
    private LocalDateTime date;
    private Integer conditionerPower;
    private Integer conditionerTemperature;
    private Integer conditionerMode;
    private Integer conditionerFanSpeed;
    private Integer conditionerFluger;
    private Integer conditionerCustom;
    private Integer conditionerSetting;
    private Integer climateMode;
    private Integer climateSeason;
    private Integer humidifier;
    private Integer radiator;

    public DatePoint() {
    }

    public DatePoint(Long id, Parameter param, LocalDateTime time, LocalDateTime date) {
        this.id = id;
        this.param = param;
        this.time = time;
        this.date = date;
    }

    public DatePoint(Parameter param, LocalDateTime time, LocalDateTime date) {
        this.param = param;
        this.time = time;
        this.date = date;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime value) {
        this.date = value;
    }

    public Integer getConditionerPower() {
        return conditionerPower;
    }

    public void setConditionerPower(Integer conditionerPower) {
        this.conditionerPower = conditionerPower;
    }

    public Integer getConditionerTemperature() {
        return conditionerTemperature;
    }

    public void setConditionerTemperature(Integer conditionerTemperature) {
        this.conditionerTemperature = conditionerTemperature;
    }

    public Integer getConditionerMode() {
        return conditionerMode;
    }

    public void setConditionerMode(Integer conditionerMode) {
        this.conditionerMode = conditionerMode;
    }

    public Integer getConditionerFanSpeed() {
        return conditionerFanSpeed;
    }

    public void setConditionerFanSpeed(Integer conditionerFanSpeed) {
        this.conditionerFanSpeed = conditionerFanSpeed;
    }

    public Integer getConditionerFluger() {
        return conditionerFluger;
    }

    public void setConditionerFluger(Integer conditionerFluger) {
        this.conditionerFluger = conditionerFluger;
    }

    public Integer getConditionerCustom() {
        return conditionerCustom;
    }

    public void setConditionerCustom(Integer conditionerCustom) {
        this.conditionerCustom = conditionerCustom;
    }

    public Integer getConditionerSetting() {
        return conditionerSetting;
    }

    public void setConditionerSetting(Integer conditionerSetting) {
        this.conditionerSetting = conditionerSetting;
    }

    public Integer getClimateMode() {
        return climateMode;
    }

    public void setClimateMode(Integer climateMode) {
        this.climateMode = climateMode;
    }

    public Integer getClimateSeason() {
        return climateSeason;
    }

    public void setClimateSeason(Integer climateSeason) {
        this.climateSeason = climateSeason;
    }

    public Integer getHumidifier() {
        return humidifier;
    }

    public void setHumidifier(Integer humidifier) {
        this.humidifier = humidifier;
    }

    public Integer getRadiator() {
        return radiator;
    }

    public void setRadiator(Integer radiator) {
        this.radiator = radiator;
    }

    public Integer[] getRegisters(){
        Integer[] result = new Integer[11];

        result[0] = conditionerPower;
        result[1] = conditionerTemperature;
        result[2] = conditionerMode;
        result[3] = conditionerFanSpeed;
        result[4] = conditionerFluger;
        result[5] = conditionerCustom;
        result[6] = conditionerSetting;
        result[7] = climateMode;
        result[8] = climateSeason;
        result[9] = radiator;
        result[10] = humidifier;

        return result;
    }

    @Override
    public String toString() {
        return "DatePoint{" +
                "id=" + id +
                ", param=" + param +
                ", time=" + time +
                ", date=" + date +
                '}';
    }
}
