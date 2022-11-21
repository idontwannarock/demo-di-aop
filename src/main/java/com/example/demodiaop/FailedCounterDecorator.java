package com.example.demodiaop;

public class FailedCounterDecorator implements Authentication {

    private final Authentication authentication;
    private final FailedCounter failedCounter;

    public FailedCounterDecorator(Authentication authentication, FailedCounter failedCounter) {
        this.authentication = authentication;
        this.failedCounter = failedCounter;
    }

    @Override
    public boolean isValid(String account, String password, String otp) {
        boolean isValid = authentication.isValid(account, password, otp);
        if (isValid) {
            reset(account);
        } else {
            increase(account);
        }
        return isValid;
    }

    void reset(String account) {
        failedCounter.reset(account);
    }

    void increase(String account) {
        failedCounter.increase(account);
    }
}