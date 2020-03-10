package com.budova.everhome.controller;

import com.budova.everhome.domain.DatePoint;
import com.budova.everhome.domain.Parameter;
import com.budova.everhome.repos.DatePointRepo;
import com.budova.everhome.service.DatePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DatePointController {

    @Autowired
    private DatePointService datePointService;

    @Autowired
    private DatePointRepo datePointRepo;

    @GetMapping("/api/datepoint/day")
    public List<DatePoint> getDataPoint(){return datePointService.getDatePointForLastDay();}

}
