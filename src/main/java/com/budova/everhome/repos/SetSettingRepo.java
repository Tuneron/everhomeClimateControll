package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetSetting;
import org.springframework.data.repository.CrudRepository;

public interface SetSettingRepo extends CrudRepository<SetSetting, Long> {

    SetSetting findFirstByParamIsOrderByTimeDesc(Parameter param);
}
