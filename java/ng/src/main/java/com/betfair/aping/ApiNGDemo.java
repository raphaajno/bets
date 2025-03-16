package com.betfair.aping;

import com.betfair.aping.exceptions.APINGException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Simplified version of the Betfair API-NG demo.
 * Always runs the JSON-RPC demo without asking for protocol.
 */
public class ApiNGDemo {

    private static Properties prop = new Properties();
    private static String applicationKey;
    private static String sessionToken;
    private static boolean debug;

    static {
        try {
            InputStream in = ApiNGDemo.class.getResourceAsStream("/apingdemo.properties");
            prop.load(in);
            in.close();

            debug = Boolean.parseBoolean(prop.getProperty("DEBUG"));

        } catch (IOException e) {
            System.out.println("Error loading the properties file: " + e.toString());
        }
    }

    public static void main(String[] args) throws APINGException {

        System.out.println("Welcome to the Betfair API NG!");

        BufferedReader inputStreamReader = null;

        // Get the AppKey and the session token
        while (applicationKey == null || applicationKey.isEmpty()) {
            System.out.println("Please insert a valid App Key: ");
            System.out.print("> ");
            inputStreamReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                applicationKey = inputStreamReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        while (sessionToken == null || sessionToken.isEmpty()) {
            System.out.println("Please insert a valid Session Token: ");
            System.out.print("> ");
            inputStreamReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                sessionToken = inputStreamReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Always run JSON-RPC demo
        System.out.println("Running JSON-RPC demo...");
        ApiNGJsonRpcDemo jsonRpcDemo = new ApiNGJsonRpcDemo();
        jsonRpcDemo.start(applicationKey, sessionToken);
    }

    public static Properties getProp() {
        return prop;
    }

    public static boolean isDebug() {
        return debug;
    }
}