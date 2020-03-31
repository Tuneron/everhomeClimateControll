package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetEnableEcoRadiator;
import org.springframework.data.repository.CrudRepository;

public interface SetEnableEcoRadiatorRepo extends CrudRepository<SetEnableEcoRadiator, Long> {

    SetEnableEcoRadiator findFirstByParamIsOrderByTimeDesc(Parameter param);

}
