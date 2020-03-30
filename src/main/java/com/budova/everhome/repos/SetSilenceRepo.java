package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetSilence;
import org.springframework.data.repository.CrudRepository;

public interface SetSilenceRepo extends CrudRepository<SetSilence, Long> {

    SetSilence findFirstByParamIsOrderByTimeDesc(Parameter param);

}
