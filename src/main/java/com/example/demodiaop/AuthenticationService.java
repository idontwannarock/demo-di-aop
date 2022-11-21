package com.example.demodiaop;

public class AuthenticationService implements Authentication {

    private final Profile profile;
    private final Hash hash;
    private final OtpService otpService;
    private final FailedCounter failedCounter;

    public AuthenticationService(Profile profile, Hash hash, OtpService otpService, FailedCounter failedCounter) {
        this.profile = profile;
        this.hash = hash;
        this.otpService = otpService;
        this.failedCounter = failedCounter;
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
            return false;
        }
    }
}
