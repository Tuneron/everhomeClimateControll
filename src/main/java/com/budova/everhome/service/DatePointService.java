package com.budova.everhome.service;

import com.budova.everhome.domain.DatePoint;
import com.budova.everhome.domain.Parameter;
import com.budova.everhome.repos.DatePointRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DatePointService {

    @Autowired
    public DatePointRepo datePointRepo;

    public List<DatePoint> getDatePointForLastDay(){
        LocalDateTime dayBefore = LocalDateTime.now().minusDays(1);
        return datePointRepo.findByParamAndTimeAfterOrderByTimeDesc(Parameter.DATE_POINT, dayBefore);
    }

}
