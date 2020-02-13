package com.budova.everhome.controller;

import com.budova.everhome.domain.Humidity;
import com.budova.everhome.service.HumidityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HumidityController {

    @Autowired
    private HumidityService humidityService;

    @GetMapping("/api/humidity/day")
    public List<Humidity> getHumidity(){
        return humidityService.getHumidityForLastDay();
    }
    
}
