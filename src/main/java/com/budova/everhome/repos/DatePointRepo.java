package com.budova.everhome.repos;

import com.budova.everhome.domain.DatePoint;
import com.budova.everhome.domain.Parameter;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.LinkedList;

public interface DatePointRepo extends CrudRepository<DatePoint, Long> {

    DatePoint findFirstByParamIsOrderByTimeDesc(Parameter param);

    LinkedList<DatePoint> findByParamAndTimeAfterOrderByTimeDesc(Parameter param, LocalDateTime after);

    LinkedList<DatePoint> findAllByParam(Parameter param);

}
