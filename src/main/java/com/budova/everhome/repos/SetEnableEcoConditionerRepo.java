package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetEnableEcoConditioner;
import org.springframework.data.repository.CrudRepository;

public interface SetEnableEcoConditionerRepo extends CrudRepository<SetEnableEcoConditioner, Long> {

    SetEnableEcoConditioner findFirstByParamIsOrderByTimeDesc(Parameter param);

}
