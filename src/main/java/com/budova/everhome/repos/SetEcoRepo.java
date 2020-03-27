package com.budova.everhome.repos;


import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetEco;
import org.springframework.data.repository.CrudRepository;

public interface SetEcoRepo extends CrudRepository<SetEco, Long>{

    SetEco findFirstByParamIsOrderByTimeDesc(Parameter param);

}
