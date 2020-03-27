package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetWindow;
import org.springframework.data.repository.CrudRepository;

public interface SetWindowRepo extends CrudRepository<SetWindow, Long> {

    SetWindow findFirstByParamIsOrderByTimeDesc(Parameter param);

}
