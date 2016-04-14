package main;

import beanConfig.BeanConfiguration;
import mq.Receiver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * Created by pizmak on 2016-04-06.
 */
@Component
@ComponentScan(basePackages = "beanConfig")
public class MainAnnotation {

    public static void main(String []args){
        System.out.println("Hello");
        //ApplicationContext context = new AnnotationConfigApplicationContext(BeanConfiguration.class);
    }
}
