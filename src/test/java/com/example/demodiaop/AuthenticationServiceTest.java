package com.example.demodiaop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

class AuthenticationServiceTest {

    private static final String defaultAccount = "Howard";

    private Profile profile;
    private Hash hash;
    private OtpService otpService;
    private Notification notification;
    private FailedCounter failedCounter;
    private Logger logger;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        profile = Mockito.mock(Profile.class);
        hash = Mockito.mock(Hash.class);
        otpService = Mockito.mock(OtpService.class);
        notification = Mockito.mock(Notification.class);
        failedCounter = Mockito.mock(FailedCounter.class);
        logger = Mockito.mock(Logger.class);
        authenticationService = new AuthenticationService(profile, hash, otpService, notification, failedCounter, logger);
    }

    @Test
    void is_valid() {
        // arrange
        givenPasswordFromProfile(defaultAccount, "my hashed password");
        givenHashedPassword(defaultAccount, "password", "my hashed password");
        givenOtp(defaultAccount, "123456");

        // act
        boolean isValid = verify(defaultAccount, "password", "123456");

        // assert
        shouldBeValid(isValid);
    }

    @Test
    void is_invalid() {
        // arrange
        givenPasswordFromProfile(defaultAccount, "my hashed password");
        givenHashedPassword(defaultAccount, "password", "my hashed password");
        givenOtp(defaultAccount, "123456");

        // act
        boolean isValid = verify(defaultAccount, "password", "wrong otp");

        // assert
        shouldBeInvalid(isValid);
    }

    private void shouldBeInvalid(boolean isValid) {
        Assertions.assertFalse(isValid);
    }

    private void shouldBeValid(boolean isValid) {
        Assertions.assertTrue(isValid);
    }

    private boolean verify(String account, String password, String otp) {
        return authenticationService.isValid(account, password, otp);
    }

    private void givenOtp(String account, String otp) {
        Mockito.when(otpService.getCurrentOtp(account)).thenReturn(otp);
    }

    private void givenHashedPassword(String account, String password, String hashedPassword) {
        Mockito.when(hash.compute(account, password)).thenReturn(hashedPassword);
    }

    private void givenPasswordFromProfile(String account, String password) {
        Mockito.when(profile.getPassword(account)).thenReturn(password);
    }
}
