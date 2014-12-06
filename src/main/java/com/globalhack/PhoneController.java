package com.globalhack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    private PhoneRepository phoneRepository;

    @RequestMapping("/phones")
    public List<Phone> phones() {
        return phoneRepository.findAll();
    }

    @RequestMapping(value = "/phones", method = RequestMethod.POST)
    public Phone phoneByNumber(String phoneNumber) {
        return new Phone(phoneNumber);
    }

    @RequestMapping(value = "/phone/{phoneNumber}", method = RequestMethod.DELETE)
    public void deletePhone(@PathVariable String phoneNumber) {
        phoneRepository.delete(phoneRepository.findByPhoneNumber(phoneNumber));
    }

}
