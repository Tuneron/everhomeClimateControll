package com.budova.everhome.conditioner;

public class ConditionerCommand {

    private Float currentTemperature = 10F;
    private Float currentHumidity = 20F;
    private Integer conditionerPower = 1;

    public ConditionerCommand() {
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
}
