package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetClimateMode;
import org.springframework.data.repository.CrudRepository;

public interface SetClimateModeRepo extends CrudRepository<SetClimateMode, Long> {

    SetClimateMode findFirstByParamIsOrderByTimeDesc(Parameter param);
}
