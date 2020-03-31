package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetEnableEcoRecuperator;
import org.springframework.data.repository.CrudRepository;

public interface SetEnableEcoRecuperatorRepo extends CrudRepository<SetEnableEcoRecuperator, Long> {

    SetEnableEcoRecuperator findFirstByParamIsOrderByTimeDesc(Parameter param);

}
