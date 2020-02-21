package com.budova.everhome.controller;

import com.budova.everhome.domain.SetCommand;
import com.budova.everhome.service.SetCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class SetCommandController {

    @Autowired
    private SetCommandService setCommandService;

    @GetMapping("/api/command/day")
    public ArrayList<SetCommand> getSetCommand() {
        return setCommandService.getSetCommandForLastDay();
    }

    @GetMapping("/api/command/all")
    public ArrayList<SetCommand> getSetCommandAll() {
        return setCommandService.getSetCommandAll();
    }
}
