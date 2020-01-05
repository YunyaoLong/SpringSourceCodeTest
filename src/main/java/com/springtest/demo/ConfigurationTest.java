package com.springtest.demo;


import com.springtest.demo.config.BeansConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Objects;

@RunWith(SpringJUnit4ClassRunner.class)
public class ConfigurationTest {
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

    /**
     * 使用注解的方式进行组件注册
     */
    @Test
    public void test2() {
        System.out.println("=============注解模式配置文件加载开始==============");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeansConfig.class);
        System.out.println("=============注解模式配置文件加载完成==============");
        System.out.println("使用注解方式进行组件注册：");
        System.out.println(context.getBean("person3"));
        System.out.println("-----------------------------------------------------");
        System.out.println("=============测试单例或者多例的相等判定，以及Bean的加载时机==============");
        System.out.println(Objects.equals(context.getBean("person3"), context.getBean("person3")));
    }

    /**
     * 扫描所有已经注册的组件
     */
    @Test
    public void test3() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeansConfig.class);
        String beansName[] = context.getBeanDefinitionNames();
        // 配置文件如果没有加入ComponentScan注解说明需要扫描的包的路径，就会只扫描Configuration及其内指定的bean到组件中
        Arrays.asList(beansName).stream().forEach(name -> System.out.println(name));
    }
}
