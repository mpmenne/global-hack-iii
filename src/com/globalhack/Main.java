package com.globalhack;
import java.util.Scanner;

import com.google.gson.stream.JsonToken;
import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.http.CookieCredentials;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;

import com.google.gson.JsonElement;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;

public class Main {

    public static void main(String[] args) {

        // Create a new console logger
        Logger logger = new Logger() {

            @Override
            public void log(String message, LogLevel level) {
                System.out.println(message);
            }
        };

        String tokenResponse = SuperFunHttpRequest.excutePost("http://bussrvstg.sensicomfort.com/api/authorize", "{\"UserName\":\"team20@globalhack.com\", \"Password\":\"globalhack\" }");
        String token = new com.google.gson.JsonParser().parse(tokenResponse).getAsJsonObject().get("Token").getAsString();

        // Connect to the server
        HubConnection conn = new HubConnection("http://bussrvstg.sensicomfort.com/realtime/", "", true, logger);
        conn.setCredentials(new CookieCredentials(".ASPXAUTH=" + token));

        // Create the hub proxy
        final HubProxy proxy = conn.createHubProxy("thermostat-v1");


//        proxy.subscribe(new Object() {
//            @SuppressWarnings("unused")
//            public void messageReceived(String name, String message) {
//                System.out.println(name + ": " + message);
//            }
//        });

        // Subscribe to the error event
        conn.error(new ErrorCallback() {

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });

        // Subscribe to the connected event
        conn.connected(new Runnable() {

            @Override
            public void run() {
                System.out.println("CONNECTED");
            }
        });

        // Subscribe to the closed event
        conn.closed(new Runnable() {

            @Override
            public void run() {
                System.out.println("DISCONNECTED");
            }
        });

        // Start the connection
        conn.start()
                .done(new Action<Void>() {

                    @Override
                    public void run(Void obj) throws Exception {
                        System.out.println("Done Connecting!");
                        proxy.subscribe("36-6f-92-ff-fe-01-3a-1b");
                        proxy.invoke("Subscribe", "36-6f-92-ff-fe-01-3a-1b");

                        SubscriptionHandler2 handler = new SubscriptionHandler2() {

                            @Override
                            public void run(Object p1, Object p2) {
                                System.out.println(String.format("ICD: %s, Model: %s", p1.toString(), p2.toString()));
                            }
                        };
                        proxy.on("update", (SubscriptionHandler2<Object,Object>) handler, Object.class, Object.class);
                    }
                });

        // Subscribe to the received event
        conn.received(new MessageReceivedHandler() {

            @Override
            public void onMessageReceived(JsonElement json) {
                System.out.println("RAW received message: " + json.toString());
            }
        });

        // Read lines and send them as messages.
        Scanner inputReader = new Scanner(System.in);

        String line = inputReader.nextLine();
        while (!"exit".equals(line)) {
            proxy.invoke("send", "Console", line).done(new Action<Void>() {

                @Override
                public void run(Void obj) throws Exception {
                    System.out.println("SENT!");
                }
            });

            line = inputReader.next();
        }

        inputReader.close();

        conn.stop();
    }
}