package com.budova.everhome.service;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetCommand;
import com.budova.everhome.repos.SetCommandRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class SetCommandService {

    @Autowired
    private SetCommandRepo setCommandRepo;

    public ArrayList<SetCommand> getSetCommandForLastDay() {
        LocalDateTime dayBefore = LocalDateTime.now().minusDays(1);
        return setCommandRepo.findByParamAndTimeAfterOrderByTimeDesc(Parameter.SET_COMMAND, dayBefore);
    }

    public ArrayList<SetCommand> getSetCommandAll() {
        return setCommandRepo.findAllByOrderByIdDesc();
    }

}
