package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetEnableEcoRecuperator;
import com.budova.everhome.domain.SetEnableEcoWaterFloor;
import org.springframework.data.repository.CrudRepository;

public interface SetEnableEcoWaterFloorRepo extends CrudRepository<SetEnableEcoWaterFloor, Long> {

    SetEnableEcoWaterFloor findFirstByParamIsOrderByTimeDesc(Parameter param);

}
