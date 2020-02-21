package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetCommand;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface SetCommandRepo extends CrudRepository<SetCommand, Long> {

    SetCommand findFirstByParamIsOrderByTimeDesc(Parameter param);

    ArrayList<SetCommand> findByParamAndTimeAfterOrderByTimeDesc(Parameter setCommand, LocalDateTime dayBefore);

    ArrayList<SetCommand> findAllByOrderByIdDesc();
}
