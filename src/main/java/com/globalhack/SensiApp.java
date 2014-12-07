package com.globalhack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created with IntelliJ IDEA.
 * User: mpmenne
 * Date: 12/6/14
 * Time: 9:42 AM
 * To change this template use File | Settings | File Templates.
 */


@ComponentScan
@EnableAutoConfiguration
public class SensiApp {

    public static void main(String[] varArgs) {
        SpringApplication application = new SpringApplication(SensiApp.class);
        application.run();
    }

}
