package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetWaterFloor;
import org.springframework.data.repository.CrudRepository;

public interface SetWaterFloorRepo extends CrudRepository<SetWaterFloor, Long> {

    SetWaterFloor findFirstByParamIsOrderByTimeDesc(Parameter param);

}
