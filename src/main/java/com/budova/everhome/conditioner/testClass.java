package com.budova.everhome.conditioner;

import java.time.LocalDateTime;

public class testClass {
    private String name;
    private Integer age;
    private LocalDateTime time;

    public testClass() {
    }

    public testClass(String name, Integer age, LocalDateTime time) {
        this.name = name;
        this.age = age;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}