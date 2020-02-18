package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetCustom;
import org.springframework.data.repository.CrudRepository;

public interface SetCustomRepo extends CrudRepository<SetCustom, Long> {

    SetCustom findFirstByParamIsOrderByTimeDesc(Parameter param);
}
