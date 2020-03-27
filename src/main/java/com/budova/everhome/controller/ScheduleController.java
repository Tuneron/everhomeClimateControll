package com.budova.everhome.controller;

import com.budova.everhome.conditioner.testClass;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

        testClass testClass1 = new testClass("Sasha", 20, LocalDateTime.now());
        testClass testClass2 = new testClass("Kirill", 25, LocalDateTime.now());
        testClass testClass3 = new testClass("Vova", 24, LocalDateTime.now());
        testClass testClass4 = new testClass("Leha", 25, LocalDateTime.now());

        ArrayList<testClass> testClasses = new ArrayList<testClass>();
        testClasses.add(testClass1);
        testClasses.add(testClass2);
        testClasses.add(testClass3);
        testClasses.add(testClass4);

        model.addAttribute("arr", testClasses);

        return "schedule";
    }

}
