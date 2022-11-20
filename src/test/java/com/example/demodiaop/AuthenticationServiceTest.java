package com.example.demodiaop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

class AuthenticationServiceTest {

    @Test
    void is_valid() {
        // arrange
        Profile profile = Mockito.mock(Profile.class);
        Hash hash = Mockito.mock(Hash.class);
        OtpService otpService = Mockito.mock(OtpService.class);
        Notification notification = Mockito.mock(Notification.class);
        FailedCounter failedCounter = Mockito.mock(FailedCounter.class);
        Logger logger = Mockito.mock(Logger.class);
        var authenticationService = new AuthenticationService(profile, hash, otpService, notification, failedCounter, logger);
        String account = "Howard";
        String password = "password";
        String otp = "123456";
        Mockito.when(profile.getPassword(account)).thenReturn("my hashed password");
        Mockito.when(hash.compute(account, password)).thenReturn("my hashed password");
        Mockito.when(otpService.getCurrentOtp(account)).thenReturn("123456");

        // act
        boolean isValid = authenticationService.isValid(account, password, otp);

        // assert
        Assertions.assertTrue(isValid);
    }
}
