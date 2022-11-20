package com.example.demodiaop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationService {

    private final ProfileDao profileDao;
    private final Sha256Adapter sha256Adapter;
    private final OtpService otpService;
    private final SlackAdapter slackAdapter;
    private final FailedCounter failedCounter;
    private final Logger logger;

    public AuthenticationService() {
        this.profileDao = new ProfileDao();
        this.sha256Adapter = new Sha256Adapter();
        this.otpService = new OtpService();
        this.slackAdapter = new SlackAdapter();
        this.failedCounter = new FailedCounter();
        this.logger = LoggerFactory.getLogger(AuthenticationService.class);
    }

    public boolean isValid(String account, String password, String otp) {
        boolean isLocked = failedCounter.isLocked(account);
        if (isLocked) {
            throw new AuthenticationException("account: " + account + " is locked");
        }

        String passwordFromDb = profileDao.getPasswordFromDb(account);
        String hashedPassword = sha256Adapter.getHashedPassword(account, password);
        String currentOtp = otpService.getCurrentOtp(account);

        if (hashedPassword.equals(passwordFromDb) && currentOtp.equals(otp)) {
            failedCounter.reset(account);
            return true;
        } else {
            failedCounter.increase(account);
            logFailedCount(account);
            slackAdapter.notify(account);
            return false;
        }
    }

    private void logFailedCount(String account) {
        int failedCount = failedCounter.getFailedCount(account);
        logger.info(String.format("account: {} failed times: {}", account, failedCount));
    }
}
