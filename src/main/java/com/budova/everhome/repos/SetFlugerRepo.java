package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetFluger;
import org.springframework.data.repository.CrudRepository;

public interface SetFlugerRepo extends CrudRepository<SetFluger, Long> {

    SetFluger findFirstByParamIsOrderByTimeDesc(Parameter param);
}
