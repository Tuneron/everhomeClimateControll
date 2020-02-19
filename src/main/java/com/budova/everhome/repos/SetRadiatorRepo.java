package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetRadiator;
import org.springframework.data.repository.CrudRepository;

public interface SetRadiatorRepo extends CrudRepository<SetRadiator, Long> {

    SetRadiator findFirstByParamIsOrderByTimeDesc(Parameter param);
}
