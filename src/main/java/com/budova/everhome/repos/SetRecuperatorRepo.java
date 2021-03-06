package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetRecuperator;
import org.springframework.data.repository.CrudRepository;

public interface SetRecuperatorRepo extends CrudRepository<SetRecuperator, Long> {

    SetRecuperator findFirstByParamIsOrderByTimeDesc(Parameter param);

}
