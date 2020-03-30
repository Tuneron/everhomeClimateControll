package com.budova.everhome.controller;

import com.budova.everhome.domain.*;
import com.budova.everhome.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private HumidityRepo humidityRepo;
    @Autowired
    private TemperatureRepo tempRepo;
    @Autowired
    private SetPowerRepo setPowerRepo;
    @Autowired
    private ValvePosRepo valvePosRepo;
    @Autowired
    private SetTemperatureRepo setTemperatureRepo;
    @Autowired
    private SetConditionerModeRepo setConditionerModeRepo;
    @Autowired
    private SetFanSpeedRepo setFanSpeedRepo;
    @Autowired
    private SetFlugerRepo setFlugerRepo;
    @Autowired
    private SetCustomRepo setCustomRepo;
    @Autowired
    private SetSettingRepo setSettingRepo;
    @Autowired
    private SetClimateModeRepo setClimateModeRepo;
    @Autowired
    private SetSeasonRepo setSeasonRepo;
    @Autowired
    private SetRadiatorRepo setRadiatorRepo;
    @Autowired
    private SetHumidifierRepo setHumidifierRepo;
    @Autowired
    private SetCommandRepo setCommandRepo;
    @Autowired
    private ConnectionRepo connectionRepo;
    @Autowired
    private SetEcoRepo setEcoRepo;
    @Autowired
    private SetWindowRepo setWindowRepo;
    @Autowired
    private SetRecuperatorRepo setRecuperatorRepo;
    @Autowired
    private SetWaterFloorRepo setWaterFloorRepo;
    @Autowired
    private SetElectricFloorRepo setElectricFloorRepo;
    @Autowired
    private SetOutsideConditionsRepo setOutsideConditionsRepo;
    @Autowired
    private SetSilenceRepo setSilenceRepo;

    @GetMapping("/home")
    public String home(Model model) {
        Humidity h = humidityRepo.findFirstByParamIsOrderByTimeDesc(Parameter.HUMIDITY);
        Temperature t1 = tempRepo.findFirstByParamIsOrderByTimeDesc(Parameter.TEMPERATURE);
        SetPower stP = setPowerRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_POWER);
        SetTemperature st = setTemperatureRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_TEMPERATURE);
        SetConditionerMode setConditionerMode = setConditionerModeRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_CONDITIONER_MODE);
        SetFanSpeed setFanSpeed = setFanSpeedRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_FAN_SPEED);
        SetFluger setFluger = setFlugerRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_FLUGER);
        SetCustom setCustom = setCustomRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_CUSTOM);
        SetSetting setSetting = setSettingRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_SETTING);
        SetClimateMode setClimateMode = setClimateModeRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_CLIMATE_MODE);
        SetSeason setSeason = setSeasonRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_SEASON);
        SetRadiator setRadiator = setRadiatorRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_RADIATOR);
        SetHumidifier setHumidifier = setHumidifierRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_HUMIDIFIER);
        SetCommand setCommand = setCommandRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_COMMAND);
        SetEco setEco = setEcoRepo.findFirstByParamIsOrderByTimeDesc(Parameter.ECO);
        SetWindow setWindow = setWindowRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_WINDOW);
        SetRecuperator setRecuperator = setRecuperatorRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_RECUPERATOR);
        SetWaterFloor setWaterFloor = setWaterFloorRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_WATER_FLOOR);
        SetElectricFloor setElectricFloor = setElectricFloorRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_ELECTRIC_FLOOR);
        SetOutsideConditions setOutsideConditions = setOutsideConditionsRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_OUTSIDE_CONDITIONS);
        SetSilence setSilence = setSilenceRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_SILENCE);
        Connection c = connectionRepo.findFirstByParamIsOrderByTimeDesc(Parameter.RAUT_CONNECTION);
        model.addAttribute("humidity", h != null ? h.getValue() : "null");
        model.addAttribute("temperature", t1 != null ? t1.getValue() : "null");
        model.addAttribute("set_power", stP != null ? stP.getValue() : "null");
        model.addAttribute("set_temperature", st != null ? st.getValue() : "null");
        model.addAttribute("set_conditioner_mode", setConditionerMode != null ? setConditionerMode.getValue() : "null");
        model.addAttribute("set_fan_speed", setFanSpeed != null ? setFanSpeed.getValue() : "null");
        model.addAttribute("set_fluger", setFluger != null ? setFluger.getValue() : "null");
        model.addAttribute("set_custom", setCustom != null ? setCustom.getValue() : "null");
        model.addAttribute("set_setting", setSetting != null ? setSetting.getValue() : "null");
        model.addAttribute("set_climate_mode", setClimateMode != null ? setClimateMode.getValue() : "null");
        model.addAttribute("set_season", setSeason != null ? setSeason.getValue() : "null");
        model.addAttribute("set_radiator", setRadiator != null ? setRadiator.getValue() : "null");
        model.addAttribute("set_humidifier", setHumidifier != null ? setHumidifier.getValue() : "null");
        model.addAttribute("set_command", setCommand != null ? setCommand.getValue() : "null");
        model.addAttribute("set_eco", setEco != null ? setEco.getValue() : "null");
        model.addAttribute("set_window", setWindow != null ? setWindow.getValue() : "null");
        model.addAttribute("set_recuperator", setRecuperator != null ? setRecuperator.getValue() : "null");
        model.addAttribute("set_water_floor", setWaterFloor != null ? setWaterFloor.getValue() : "null");
        model.addAttribute("set_electric_floor", setElectricFloor != null? setElectricFloor.getValue() : "null");
        model.addAttribute("set_outside_conditions", setOutsideConditions != null? setOutsideConditions.getValue() : "null");
        model.addAttribute("set_silence", setSilence != null ? setSilence.getValue() : "null");
        model.addAttribute("connection", c != null ? c.getValue() : "null");
        List<Temperature> temps = tempRepo.findTop10ByParamIsOrderByTimeDesc(Parameter.TEMPERATURE);
        StringBuilder sb = new StringBuilder();
        for (Temperature t : temps) {
            sb.append(t.toString());
        }
        model.addAttribute("temperature3", sb.toString());
        return "home";
    }

}
