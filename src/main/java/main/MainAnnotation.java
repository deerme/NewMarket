package main;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * Created by pizmak on 2016-04-06.
 */
@Component
@ComponentScan(basePackages = "beanConfig")
public class MainAnnotation {

    public static void main(String []args){
    }
}
