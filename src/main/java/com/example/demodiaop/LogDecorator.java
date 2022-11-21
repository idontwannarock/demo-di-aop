package com.example.demodiaop;

import org.slf4j.Logger;

public class LogDecorator extends AuthenticationDecoratorBase {

    private final FailedCounter failedCounter;
    private final Logger logger;

    public LogDecorator(Authentication authentication, FailedCounter failedCounter, Logger logger) {
        super(authentication);
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