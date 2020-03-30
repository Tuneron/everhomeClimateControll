package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetOutsideConditions;
import org.springframework.data.repository.CrudRepository;

public interface SetOutsideConditionsRepo extends CrudRepository<SetOutsideConditions, Long> {

    SetOutsideConditions findFirstByParamIsOrderByTimeDesc(Parameter param);

}
