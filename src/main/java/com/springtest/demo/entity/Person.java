package com.springtest.demo.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ToString
public class Person {
    private Integer age;
    private String name;
    public  Person() {
        System.out.println("========一个Person对象被创建==========");
    }
}
