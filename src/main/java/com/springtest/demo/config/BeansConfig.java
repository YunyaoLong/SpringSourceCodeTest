package com.springtest.demo.config;

import com.springtest.demo.config.typefilter.PersonTypeFilter;
import com.springtest.demo.entity.Person;
import com.springtest.demo.service.PersonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

// 配置类等效配置文件
@Configuration(value = "beansConfig2")  // 表示这是一个配置类
//@ComponentScan(value = "com.springtest.demo" )  // 表示需要扫描的包，可以尝试注释该行然后运行ConfigurationTest.test3查看区别
@ComponentScan(value = "com.springtest.demo",
        // 定制扫描规则，默认扫描@Controller, @Service, @Repository, @Component，控制排除哪些组件不需要被包含
         //excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = Controller.class),
        useDefaultFilters = false,
        includeFilters = {// 定制扫描规则，控制只扫描哪些组件，需要关闭默认Filter
                // @Filter(type = FilterType.ANNOTATION, classes = {Controller.class}), // FilterType.ANNOTATION 按照注解方式进行操作
                //@Filter(type = FilterType.ASSIGNABLE_TYPE,classes = {PersonService.class}),  // FilterType.ASSIGNABLE_TYPE 按照指定类型的方式进行组件扫描
                // FilterType.ASPECTJ  比较不常用，指定AspectJ表达式进行组件扫描
                // FilterType.REGEX  指定符合正则的包进行扫描
                // FilterType.CUSTOM // 使用自定义规则
                @Filter(type = FilterType.CUSTOM,classes = {PersonTypeFilter.class})  // 使用自定义规则
        }
)

// Component是一个可重复定义组件: @Repeatable
//@ComponentScan
public class BeansConfig {
    @Bean(name = "person3")
    public Person person01() {
        Person person = new Person();
        person.setAge(20);
        person.setName("lisi");
        return person;
    }
}
