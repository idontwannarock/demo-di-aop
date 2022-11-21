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
        checkAccountLocked(account);

        String passwordFromDb = profile.getPassword(account);
        String hashedPassword = hash.compute(account, password);
        String currentOtp = otpService.getCurrentOtp(account);

        return hashedPassword.equals(passwordFromDb) && currentOtp.equals(otp);
    }

    private void checkAccountLocked(String account) {
        boolean isLocked = failedCounter.isLocked(account);
        if (isLocked) {
            throw new AuthenticationException("account: " + account + " is locked");
        }
    }
}
