package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetSeason;
import org.springframework.data.repository.CrudRepository;

public interface SetSeasonRepo extends CrudRepository<SetSeason, Long> {

    SetSeason findFirstByParamIsOrderByTimeDesc(Parameter param);

}
