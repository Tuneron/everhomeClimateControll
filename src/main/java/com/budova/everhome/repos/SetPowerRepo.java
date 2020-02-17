package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetPower;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.LinkedList;

public interface SetPowerRepo extends CrudRepository<SetPower, Long> {

    SetPower findFirstByParamIsOrderByTimeDesc(Parameter param);

    LinkedList<SetPower> findTop10ByParamIsOrderByTimeDesc(Parameter param);

    LinkedList<SetPower> findByParamAndTimeAfterOrderByTimeDesc(Parameter param, LocalDateTime after);
}
