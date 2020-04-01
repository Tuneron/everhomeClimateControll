package com.budova.everhome.repos;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetEnableEcoElectricFloor;
import org.springframework.data.repository.CrudRepository;

public interface SetEnableEcoElectricFloorRepo extends CrudRepository<SetEnableEcoElectricFloor, Long> {

    SetEnableEcoElectricFloor findFirstByParamIsOrderByTimeDesc(Parameter param);

}

