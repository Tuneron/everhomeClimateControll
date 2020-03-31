package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetNight;
import org.springframework.data.repository.CrudRepository;

public interface SetNightRepo extends CrudRepository<SetNight, Long> {

    SetNight findFirstByParamIsOrderByTimeDesc(Parameter param);

}
