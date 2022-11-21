package com.example.demodiaop;

import org.slf4j.Logger;

public class LogDecorator implements Authentication {

    private final Authentication authentication;
    private final FailedCounter failedCounter;
    private final Logger logger;

    public LogDecorator(Authentication authentication, FailedCounter failedCounter, Logger logger) {
        this.authentication = authentication;
        this.failedCounter = failedCounter;
        this.logger = logger;
    }

    @Override
    public boolean isValid(String account, String password, String otp) {
        boolean isValid = authentication.isValid(account, password, otp);
        if (!isValid) {
            logFailedCount(account);
        }
        return isValid;
    }

    void logFailedCount(String account) {
        int failedCount = failedCounter.getFailedCount(account);
        logger.info("account: {} failed times: {}", account, failedCount);
    }
}