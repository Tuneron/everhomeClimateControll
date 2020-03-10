package com.budova.everhome.controller;

import com.budova.everhome.domain.DatePoint;
import com.budova.everhome.domain.Parameter;
import com.budova.everhome.driver.RautControllerDriver;
import com.budova.everhome.repos.DatePointRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ScheduleController {

    @Autowired
    private DatePointRepo datePointRepo;

    @RequestMapping(value = "/schedule", method=RequestMethod.GET)
    public String showAllData(Number getPower,
                              Number getTemp,
                              Number getMode,
                              Number getFanSpeed,
                              Number getFluger,
                              Number getCustom,
                              Number getSetting,
                              Number getClimateMode,
                              Number getSeason,
                              Number getRadiator,
                              Number getHumidifier,
                              String getDate,
                              String getTime,
                              ModelMap model) {
        model.addAttribute("count", "variable is " + getPower);
        if(getPower != null
                && getTemp != null
                && getMode != null
                && getSetting != null
                && getClimateMode !=null
                && getSeason != null
                && getRadiator != null
                && getHumidifier != null
                && getDate != null
                && getTime != null) {
            DatePoint datePoint = new DatePoint(Parameter.DATE_POINT, LocalDateTime.now(), LocalDateTime.parse(getDate + "T" + getTime + ":00.000"));
            datePoint.setConditionerPower(getPower.intValue());
            datePoint.setConditionerTemperature(getTemp.intValue());
            datePoint.setConditionerMode(getMode.intValue());
            datePoint.setConditionerFanSpeed(getFanSpeed == null ? 0 : getFanSpeed.intValue());
            datePoint.setConditionerFluger(getFluger == null ? 0 : getFluger.intValue());
            datePoint.setConditionerCustom(getCustom == null ? 0 : getCustom.intValue());
            datePoint.setConditionerSetting(getSetting.intValue());
            datePoint.setClimateMode(getClimateMode.intValue());
            datePoint.setClimateSeason(getSeason.intValue());
            datePoint.setRadiator(getRadiator.intValue());
            datePoint.setHumidifier(getHumidifier.intValue());
            datePointRepo.save(datePoint);
        }

        return "schedule";
    }

}
