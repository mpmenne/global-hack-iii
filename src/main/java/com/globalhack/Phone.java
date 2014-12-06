package com.globalhack;

/**
 * Created with IntelliJ IDEA.
 * User: mpmenne
 * Date: 12/6/14
 * Time: 8:49 AM
 * To change this template use File | Settings | File Templates.
 */


import org.springframework.data.annotation.Id;

public class Phone {


    @Id
    private String id;

    private String phoneNumber;

    public Phone() {
    }

    public Phone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return phoneNumber;
    }
}
