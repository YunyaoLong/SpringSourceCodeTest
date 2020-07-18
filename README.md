# LongyunYao/SpringSourceCodeTest

当前有通过配置文件或者通过注解注入两种方式进行组件的注册。对于配置文件注入方式，可以参考`beans.xml`中的相关配置。 😀 

## 文件配置方式进行组件注册

使用和包裹所有需要注册的组件，扫描过程中使用进行扫描规则定义。

\*\*注1：\*\*如果需要支持标签，需要引入 `xmlns:context="http://www.springframework.org/schema/context"`。同时需要在`xsi:schemaLocation`中引入`spring.context`的`xsd`地址。`http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd`

本次`beans.xml`内容如下：

```text
xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">

    
    <context:component-scan base-package="com.springtest.demo">context:component-scan>
    <bean id="person" class="com.springtest.demo.entity.Person" name="person2" >
        <property name="age" value="18"/>
        <property name="name" value="zhangsan" />
    bean>
beans>
```

上文定义了本次使用配置文件进行组件注册时所用的相关xml配置。

对应的测试类为

```text
/**
 * 使用xml直接注入的方式进行组件注册
 */
@Test
public void test1() {
    ClassPathXmlApplicationContext context =  new ClassPathXmlApplicationContext("beans.xml");
    System.out.println("使用xml直接注入的方式进行组件注册：");
    System.out.println(context.getBean("person"));
    System.out.println("-----------------------------------------------------");
    System.out.println("查看所有被扫描注册的组件：");
    Arrays.asList(context.getBeanDefinitionNames()).stream().forEach(name -> System.out.println("---->"+name));
}
```

从上我们可以发现使用配置进行组件注册的方式配置上维护比较直观，比较符合程序开发过程中配置化的思想。但是缺点也是非常明显的，xml配置的先天劣势决定了我们如果要自定义扫描规则会带来非常多不便。因此就有第二种组件注册的方式。

## 注入方式进行组件注册

废话不多说，直接上源码。相关详细测试需要在项目中运行才能直观感受到（ConfigurationTest.test3\)，这里只做粗略解释。相关内容需要阅读注释和各个注解的源码。

```text
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
```

上述代码中对`@ComponentScan`中各种扫描规则以及扫描过程中过滤器`@Filter`的用法做了一些demo。详细实现需要再运行源码体会一下。主要使用内容如下：

* `ComponentScan`注解：
  * `includeFilters`需要包含的扫描范围定义
  * `excludeFilters` 不需要包含的扫描范围定义
  * `@Filte`注解：用于上述两种扫描规则的具体过滤规则定义，其`Filterype`分为如下几种过滤规则：
    * `FilterType.ANNOTATION`：对不同的注解进行扫描
    * `FilterType.ASSIGNABLE_TYPE`：对不同的类名进行规则扫描
    * `FilterType.ASPECTJ` ： 比较不常用，指定AspectJ表达式进行组件扫描
    * `FilterType.REGEX`： 指定符合正则的包进行扫描
    * `FilterType.CUSTOM`：使用自定义规则，上述集中规则应该均由该方式实现

**注1：** 由于Config自身首先需要进行注册然后才能进行相关的执行，因此Config类本身是肯定会被注册的。

