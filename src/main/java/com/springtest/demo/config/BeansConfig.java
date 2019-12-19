package com.springtest.demo.config;

import com.springtest.demo.entity.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// 配置类等效配置文件
@Configuration(value = "beansConfig2")  // 表示这是一个配置类
@ComponentScan(value = "com.springtest.demo")  // 表示需要扫描的包，可以尝试注释该行然后运行ConfigurationTest.test3查看区别
public class BeansConfig {
    @Bean(name = "person3")
    public Person person01() {
        Person person = new Person();
        person.setAge(20);
        person.setName("lisi");
        return person;
    }
}
