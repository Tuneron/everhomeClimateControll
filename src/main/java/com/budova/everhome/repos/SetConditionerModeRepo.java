package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetConditionerMode;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.LinkedList;

public interface SetConditionerModeRepo extends CrudRepository<SetConditionerMode, Long> {

    SetConditionerMode findFirstByParamIsOrderByTimeDesc(Parameter param);

    LinkedList<SetConditionerMode> findTop10ByParamIsOrderByTimeDesc(Parameter param);

    LinkedList<SetConditionerMode> findByParamAndTimeAfterOrderByTimeDesc(Parameter param, LocalDateTime after);

}
