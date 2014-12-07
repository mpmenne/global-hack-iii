package com.globalhack;

import com.globalhack.util.SuperFunHttpRequest;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import microsoft.aspnet.signalr.client.*;
import microsoft.aspnet.signalr.client.http.CookieCredentials;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

@Service
public class SensiService {

    private static final Object mSync = new Object();

    private String icd;

    private HubProxy proxy;

    public SensiState sensiState = new SensiState();

    public String getIcd() {
        return icd;
    }

    public HubProxy getProxy() {
        return proxy;
    }

    public void setProxy(HubProxy proxy) {
        this.proxy = proxy;
    }

    public void setIcd(String icd) {
        this.icd = icd;
    }

    @PostConstruct
    public void init() throws ExecutionException, InterruptedException {

        this.sensiState.setCurrentTemperature(0.0);
        this.sensiState.setCoolSetPoint(0.0);
        this.sensiState.setHeatSetPoint(0.0);
        this.sensiState.setOperatingMode("Not Connected");
        this.sensiState.setAway(false);

        this.setIcd("36-6f-92-ff-fe-01-3a-1b");

        //   Create a new console logger
        Logger lawger = new Logger() {

            @Override
            public void log(String message, LogLevel level) {
                System.out.println(message);
            }
        };

        String tokenResponse = SuperFunHttpRequest.excutePost("http://bussrvstg.sensicomfort.com/api/authorize", "{\"UserName\":\"team20@globalhack.com\", \"Password\":\"globalhack\" }");
        String token = new com.google.gson.JsonParser().parse(tokenResponse).getAsJsonObject().get("Token").getAsString();

        // Connect to the server
        HubConnection conn = new HubConnection("http://bussrvstg.sensicomfort.com/realtime/", "", true, lawger);
        conn.setCredentials(new CookieCredentials(".ASPXAUTH=" + token));

        // Create the hub proxy
        HubProxy proxy = conn.createHubProxy("thermostat-v1");

        this.setProxy(proxy);

        //  Subscribe to the error event
//        conn.error(new ErrorCallback() {
//
//            @Override
//            public void onError(Throwable error) {
//                error.printStackTrace();
//            }
//        });
//
//        // Subscribe to the connected event
//        conn.connected(new Runnable() {
//
//            @Override
//            public void run() {
//                System.out.println("CONNECTED");
//            }
//        });
//
//        // Subscribe to the closed event
//        conn.closed(new Runnable() {
//
//            @Override
//            public void run() {
//                System.out.println("CLOSED");
//            }
//        });
//
//
//        // Subscribe to the received event
//        conn.received(new MessageReceivedHandler() {
//
//            @Override
//            public void onMessageReceived(JsonElement json) {
//                System.out.println("RAW received message: " + json.toString());
//            }
//        });
//
        // Start the connection

        SignalRFuture<Void> awaitConnection = conn.start();
        try {
            awaitConnection.get();
            System.out.println("Done Connecting!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        createSubscription();

//        Read lines and send them as messages.
        Scanner inputReader = new Scanner(System.in);


        // This causes problems
//        String line = inputReader.nextLine();
//        while (!"exit".equals(line)) {
//            proxy.invoke("send", "Console", line).done(new Action<Void>() {
//
//                @Override
//                public void run(Void obj) throws Exception {
//                    System.out.println("SENT!");
//                }
//            });
//            line = inputReader.next();
//        }
        inputReader.close();

//        conn.stop();
    }

    private void createSubscription() throws ExecutionException, InterruptedException {
        HubProxy proxy = this.getProxy();
        proxy.invoke("Subscribe", this.getIcd()).get();

        SubscriptionHandler2 handler = new SubscriptionHandler2() {

            @Override
            public void run(Object o, Object o2) {


                JsonObject jsonObject = null;
                try {
                    JsonElement jsonElement = new com.google.gson.JsonParser().parse(o2.toString());
                    jsonObject = jsonElement.getAsJsonObject();
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    System.out.println("Problem Parsing incoming message");
                }

                // Store updates in state vars. Beware. Shitty Code Ahead Matey.

                // Store Current Temperature
                storeCurrentTemperature(jsonObject);

                // Store Heat Set Point
                storeHeatSetPoint(jsonObject);

                // Store Cool Set Point
                storeCoolSetPoint(jsonObject);

                // Store Operating Mode
                storeOperatingMode(jsonObject);

                System.out.println(String.format("IcdString: %s, Model: %s", o.toString(), o2.toString()));

            }


        };
        proxy.on("update", (SubscriptionHandler2<Object, JsonElement>) handler, Object.class, JsonElement.class);
        proxy.on("online", (SubscriptionHandler2<Object, JsonElement>) handler, Object.class, JsonElement.class);
    }

    private void storeOperatingMode(JsonObject jsonObject) {
        try {
            sensiState.setOperatingMode(jsonObject.get("OperationalStatus").getAsJsonObject().get("OperatingMode").toString().replace("\"", ""));
        } catch (NullPointerException e) {
            System.out.println("Empty Update Value");
        }
    }

    private void storeCoolSetPoint(JsonObject jsonObject) {
        try {
            sensiState.setCoolSetPoint((double) Integer.parseInt(jsonObject.get("EnvironmentControls").getAsJsonObject().get("CoolSetpoint").getAsJsonObject().get("F").toString()));
        } catch (NullPointerException e) {
            System.out.println("Empty Update Value");
        }
    }

    private void storeHeatSetPoint(JsonObject jsonObject) {
        try {
            sensiState.setHeatSetPoint((double) Integer.parseInt(jsonObject.get("EnvironmentControls").getAsJsonObject().get("HeatSetpoint").getAsJsonObject().get("F").toString()));
        } catch (NullPointerException e) {
            System.out.println("Empty Update Value");
        }
    }

    private void storeCurrentTemperature(JsonObject jsonObject) {
        try {
            sensiState.setCurrentTemperature((double) Integer.parseInt(jsonObject.get("OperationalStatus").getAsJsonObject().get("Temperature").getAsJsonObject().get("F").toString()));
        } catch (NullPointerException e) {
            System.out.println("Empty Update Value");
        }
    }

    private void performSensiMethod1(String methodName, Object p1) throws InterruptedException, ExecutionException {

        HubProxy proxy = this.getProxy();
        proxy.invoke(Object.class, methodName, this.getIcd(), p1).done(new Action<Object>() {

            @Override
            public void run(Object o) throws Exception {
                System.out.println("Sensi Method Performed");
            }
        }).get();
    }

    private void performSensiMethod2(String methodName, Object p1, Object p2) throws InterruptedException, ExecutionException {

        HubProxy proxy = this.getProxy();
        proxy.invoke(Object.class, methodName, this.getIcd(), p1, p2).done(new Action<Object>() {

            @Override
            public void run(Object o) throws Exception {
                synchronized (mSync) {
                    System.out.println("Sensi Method Performed");
                }
            }
        }).get();
    }

    public void setHeat(Integer temp) throws InterruptedException, ExecutionException {
        performSensiMethod2("SetHeat", temp, "F");
    }

    public void setCool(Integer temp) throws InterruptedException, ExecutionException {
        performSensiMethod2("SetCool", temp, "F");
    }

    public void setSystemMode(String mode) throws InterruptedException, ExecutionException {
        performSensiMethod1("SetSystemMode", mode);
    }
}
