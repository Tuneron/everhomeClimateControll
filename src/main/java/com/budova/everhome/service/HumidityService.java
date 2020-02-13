package com.budova.everhome.service;

import com.budova.everhome.domain.Humidity;
import com.budova.everhome.domain.Parameter;
import com.budova.everhome.repos.HumidityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HumidityService {

@Autowired
    public HumidityRepo humidityRepo;

    public List<Humidity> getHumidityForLastDay(){
        LocalDateTime dayBefore = LocalDateTime.now().minusDays(1);
        return humidityRepo.findByParamAndTimeAfterOrderByTimeDesc(Parameter.HUMIDITY, dayBefore);
    }
}
