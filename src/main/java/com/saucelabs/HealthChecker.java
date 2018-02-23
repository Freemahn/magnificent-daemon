package com.saucelabs;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Entry point of program.
 * Runs health-checker in separate daemon thread.
 * Possible arguments (both optional):
 * args[0] : url of the service
 * args[1] : delay between checking health, milliseconds
 */

public class HealthChecker {
    private static final Logger logger = LogManager.getLogger(HealthChecker.class);

    public static void main(String[] args) throws InterruptedException {
        Thread dt;
        if (args.length == 2) {
            String serviceUrl = args[0];
            int sleepTime = 5000;
            try {
                sleepTime = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                logger.log(Level.ERROR, "delay should be integer");
                System.exit(-1);
            }
            dt = new Thread(new HealthCheckerThread(serviceUrl, sleepTime), "health-checker");
        } else {
            dt = new Thread(new HealthCheckerThread(), "health-checker");
        }

        dt.setDaemon(true);
        logger.log(Level.INFO, "health-checker started");
        dt.start();
        dt.join();
        logger.log(Level.INFO, "health-checker stopped");
    }
}
