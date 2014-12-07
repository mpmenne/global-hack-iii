package com.globalhack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class SensiController {

    @Autowired
    private SensiService sensiService;

    public Boolean isHeating() {
        if (sensiService.sensiState.getOperatingMode().toLowerCase().equals("heat")) {
            return true;
        }
        else return false;
    }

    @RequestMapping(value="/desiredTemperature", method = RequestMethod.POST, params="temp")
    public String setTemperature(String temp) throws InterruptedException, ExecutionException {
        Integer tempInt = Integer.parseInt(temp);
        if (isHeating()) {
            sensiService.setHeat(tempInt);
        }
        else {
            sensiService.setCool(tempInt);
        }
        return temp;
    }

    @RequestMapping(value="/temperature", method = RequestMethod.GET)
    public String getTemperature() throws InterruptedException, ExecutionException {

        return sensiService.sensiState.getCurrentTemperature().toString();
    }

    @RequestMapping(value="/desiredTemperature", method = RequestMethod.GET)
    public String getDesiredTemperature() throws InterruptedException, ExecutionException {
        if (isHeating()) {
            return sensiService.sensiState.getHeatSetPoint().toString();
        }
        else {
            return sensiService.sensiState.getCoolSetPoint().toString();

        }
    }

    @RequestMapping(value="/duration", method = RequestMethod.GET)
    public String getDuration() throws InterruptedException, ExecutionException {
        return sensiService.sensiState.getOperatingSince();
    }

    @RequestMapping(value="/mode", method = RequestMethod.GET)
    public String getMode() throws InterruptedException, ExecutionException {
        return sensiService.sensiState.getOperatingMode();
    }

    @RequestMapping(value="/mode", method = RequestMethod.POST, params="mode")
    public String setMode(String mode) throws InterruptedException, ExecutionException {

        sensiService.setSystemMode(mode);

        return mode;
    }

    @RequestMapping(value="/away", method = RequestMethod.GET)
    public String getAway() throws InterruptedException, ExecutionException {
        return sensiService.sensiState.getAway().toString();
    }

    @RequestMapping(value="/away", method = RequestMethod.POST, params="active")
    public String setAway(String active) throws InterruptedException, ExecutionException {

        String stateText = active.toLowerCase();

        boolean isActive = false;
        try {
            isActive = Boolean.parseBoolean(stateText);
        } catch (Exception e) {
            return "true or false";
        }

        // Do not start if it is already started
        if (isActive && !sensiService.sensiState.getAway()) {
            if (isHeating()) {
                Double newTemperature = sensiService.sensiState.getHeatSetPoint() - 6.0;
                sensiService.setHeat(newTemperature.intValue());
            }
            else {
                Double  newTemperature = sensiService.sensiState.getCoolSetPoint() + 6.0;
                sensiService.setCool(newTemperature.intValue());
            }
        }

        sensiService.sensiState.setAway(isActive);

        return stateText;
    }


}
