package com.budova.everhome.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Parameter {
    RAUT_CONNECTION(1),
    TEMPERATURE(2),
    HUMIDITY(3),
    SET_POWER(4),
    SET_TEMPERATURE(5),
    SET_CONDITIONER_MODE(6),
    SET_FAN_SPEED(7),
    SET_FLUGER(8),
    SET_CUSTOM(9),
    SET_SETTING(10),
    SET_CLIMATE_MODE(11),
    SET_SEASON(12),
    SET_RADIATOR(13),
    SET_HUMIDIFIER(14),
    SET_COMMAND(15),
    DATE_POINT(16),
    ECO(17),
    SET_WINDOW(18),
    SET_RECUPERATOR(19),
    SET_WATER_FLOOR(20),
    SET_ELECTRIC_FLOOR(21),
    SET_OUTSIDE_CONDITIONS(22),
    SET_SILENCE(23),
    SET_NIGHT(24);

    private Integer id;
    private String name;

    private static final Map<Integer, Parameter> STATIC_HOLDER = Arrays
            .stream(Parameter.values())
            .collect(Collectors.toMap(Parameter::getId, Function.identity())
    );

    Parameter(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    Parameter(Integer id) {
        this.id = id;
        this.name = "";
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Parameter of(Integer id) {
        return STATIC_HOLDER.get(id);
    }
}
