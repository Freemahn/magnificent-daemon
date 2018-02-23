package com.saucelabs;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Entry point of program.
 * Runs health-checker in separate daemon thread
 */

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        Thread dt = new Thread(new HealthCheckerThread(), "health-checker");
        dt.setDaemon(true);
        logger.log(Level.INFO, "health-checker started");
        dt.start();

        //continue program
        dt.join();
        logger.log(Level.INFO, "health-checker stopped");
    }
}
