package com.budova.everhome.repos;

import com.budova.everhome.domain.Humidity;
import com.budova.everhome.domain.Parameter;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.LinkedList;

public interface HumidityRepo extends CrudRepository<Humidity, Long> {

    Humidity findFirstByParamIsOrderByTimeDesc(Parameter param);

    LinkedList<Humidity> findTop10ByParamIsOrderByTimeDesc(Parameter param);

    LinkedList<Humidity> findByParamAndTimeAfterOrderByTimeDesc(Parameter param, LocalDateTime after);

}
