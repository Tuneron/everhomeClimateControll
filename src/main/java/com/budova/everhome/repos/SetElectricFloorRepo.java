package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetElectricFloor;
import org.springframework.data.repository.CrudRepository;

public interface SetElectricFloorRepo extends CrudRepository<SetElectricFloor, Long> {

    SetElectricFloor findFirstByParamIsOrderByTimeDesc(Parameter param);

}
