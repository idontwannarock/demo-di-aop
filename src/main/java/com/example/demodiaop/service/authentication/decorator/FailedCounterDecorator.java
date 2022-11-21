package com.example.demodiaop.service.authentication.decorator;

import com.example.demodiaop.service.authentication.Authentication;
import com.example.demodiaop.service.authentication.gateway.FailedCounter;
import com.example.demodiaop.service.authentication.exception.AuthenticationException;

public class FailedCounterDecorator extends AuthenticationDecoratorBase {

    private final FailedCounter failedCounter;

    public FailedCounterDecorator(Authentication authentication, FailedCounter failedCounter) {
        super(authentication);
        this.failedCounter = failedCounter;
    }

    @Override
    public boolean isValid(String account, String password, String otp) {
        checkAccountLocked(account);
        boolean isValid = authentication.isValid(account, password, otp);
        if (isValid) {
            reset(account);
        } else {
            increase(account);
        }
        return isValid;
    }

    private void checkAccountLocked(String account) {
        boolean isLocked = failedCounter.isLocked(account);
        if (isLocked) {
            throw new AuthenticationException("account: " + account + " is locked");
        }
    }

    void reset(String account) {
        failedCounter.reset(account);
    }

    void increase(String account) {
        failedCounter.increase(account);
    }
}