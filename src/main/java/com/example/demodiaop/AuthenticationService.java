package com.example.demodiaop;

import org.slf4j.Logger;

public class AuthenticationService implements Authentication {

    private final Profile profile;
    private final Hash hash;
    private final OtpService otpService;
    private final Notification notification;
    private final FailedCounter failedCounter;
    private final Logger logger;

    public AuthenticationService(Profile profile, Hash hash, OtpService otpService, Notification notification, FailedCounter failedCounter, Logger logger) {
        this.profile = profile;
        this.hash = hash;
        this.otpService = otpService;
        this.notification = notification;
        this.failedCounter = failedCounter;
        this.logger = logger;
    }

    @Override
    public boolean isValid(String account, String password, String otp) {
        boolean isLocked = failedCounter.isLocked(account);
        if (isLocked) {
            throw new AuthenticationException("account: " + account + " is locked");
        }

        String passwordFromDb = profile.getPassword(account);
        String hashedPassword = hash.compute(account, password);
        String currentOtp = otpService.getCurrentOtp(account);

        if (hashedPassword.equals(passwordFromDb) && currentOtp.equals(otp)) {
            failedCounter.reset(account);
            return true;
        } else {
            failedCounter.increase(account);
            logFailedCount(account);
            notification.notify(account);
            return false;
        }
    }

    private void logFailedCount(String account) {
        int failedCount = failedCounter.getFailedCount(account);
        logger.info("account: {} failed times: {}", account, failedCount);
    }
}
