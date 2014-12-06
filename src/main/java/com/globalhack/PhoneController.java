package com.globalhack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * User: mpmenne
 * Date: 12/6/14
 * Time: 7:55 AM
 * To change this template use File | Settings | File Templates.
 */

@RestController
public class PhoneController {

    @Autowired
    @Lazy
    private ExampleRepository exampleRepository;

    @RequestMapping("/")
    public String home() {
        new Phone("3144779816");
        return "Heyo";
    }

}
