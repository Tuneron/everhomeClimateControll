package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetHumidifier;
import org.springframework.data.repository.CrudRepository;

public interface SetHumidifierRepo extends CrudRepository<SetHumidifier, Long> {

    SetHumidifier findFirstByParamIsOrderByTimeDesc(Parameter param);
}
