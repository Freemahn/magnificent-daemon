package com.saucelabs;

import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * This class checks if the Magnificent service is up every 5 seconds
 */
class HealthCheckerThread implements Runnable {

    private static final Logger logger = LogManager.getLogger(HealthCheckerThread.class);

    private boolean running = false;

    /**
     * Delay between checking service status, in milliseconds
     */
    private int sleepTime;
    private String serviceUrl;

    public HealthCheckerThread() {
        this.sleepTime = 5_000;
        this.serviceUrl = "http://localhost:12345";
    }

    public HealthCheckerThread(String serviceUrl, int sleepTime) {
        if (sleepTime < 0) {
            throw new IllegalArgumentException("SleepTime should be positive, now " + sleepTime);
        }
        //todo implement serviceUrl validation if needed
        this.sleepTime = sleepTime;
        this.serviceUrl = serviceUrl;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            checkHealth();
        }
    }

    private void checkHealth() {
        try {
            this.sendGet();
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends request to serviceUrl and logs the result of this request
     */
    private void sendGet() {

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet(serviceUrl);

            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();

                HttpEntity entity = response.getEntity();
                String result = entity != null ? EntityUtils.toString(entity) : null;
                if (status >= 200 && status < 300) {
                    logger.log(Level.INFO, "Magnificent is UP, status code: " + status);
                } else {
                    logger.log(Level.WARN, "Magnificent is DOWN, status code: " + status);
                }
                return result;
            };
            httpclient.execute(httpget, responseHandler);
        } catch (IOException e) {
            logger.log(Level.ERROR, "Magnificent is not responding");
        }
    }
}