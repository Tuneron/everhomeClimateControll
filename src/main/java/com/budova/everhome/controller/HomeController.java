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
    private ConnectionRepo connectionRepo;

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
