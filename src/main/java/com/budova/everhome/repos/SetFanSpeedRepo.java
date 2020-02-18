package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetFanSpeed;
import org.springframework.data.repository.CrudRepository;

public interface SetFanSpeedRepo extends CrudRepository<SetFanSpeed, Long> {

    SetFanSpeed findFirstByParamIsOrderByTimeDesc(Parameter param);
}
