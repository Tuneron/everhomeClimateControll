package com.budova.everhome.domain;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Table(indexes = {
        @Index(name = "idx_parameter_id_time", columnList = "parameter_id,time")
})

public class SetCommand {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "parameter_id")
    private Parameter param;
    private LocalDateTime time;
    private Float value;
    private Float currentTemperature;
    private Float currentHumidity;
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


    private static final float MIN_VALUE = 0F;
    private static final float MAX_VALUE = 1F;

    public SetCommand() {
    }

    public SetCommand(Long id, Parameter param, LocalDateTime time, Float value) {
        this.id = id;
        this.param = param;
        this.time = time;
        this.value = value;
    }

    public SetCommand(Parameter param, LocalDateTime time, Float value) {
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

    public Float getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(Float currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public Float getCurrentHumidity() {
        return currentHumidity;
    }

    public void setCurrentHumidity(Float currentHumidity) {
        this.currentHumidity = currentHumidity;
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
        Integer[] result = new Integer[13];

        result[0] = currentTemperature.intValue();
        result[1] = currentHumidity.intValue();
        result[2] = conditionerPower;
        result[3] = conditionerTemperature;
        result[4] = conditionerMode;
        result[5] = conditionerFanSpeed;
        result[6] = conditionerFluger;
        result[7] = conditionerCustom;
        result[8] = conditionerSetting;
        result[9] = climateMode;
        result[10] = climateSeason;
        result[11] = radiator;
        result[12] = humidifier;

        return result;
    }

    public static float inBorder(float value){
        if (value < MIN_VALUE) return MIN_VALUE;
        else
        if(value > MAX_VALUE) return MAX_VALUE;
        else
            return value;
    }

    public static float inBorder(SetCommand value){
        if (value.getValue() < MIN_VALUE) return MIN_VALUE;
        else
        if(value.getValue() > MAX_VALUE) return MAX_VALUE;
        else
            return value.getValue();
    }

    @Override
    public String toString() {
        return "SetCommand{" +
                "id=" + id +
                ", param=" + param +
                ", time=" + time +
                ", value=" + value +
                '}';
    }
}
