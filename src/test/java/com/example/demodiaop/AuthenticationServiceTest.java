package com.example.demodiaop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;

class AuthenticationServiceTest {

    private static final String defaultAccount = "Howard";
    private static final Integer defaultFailedCount = 34;

    private Profile profile;
    private Hash hash;
    private OtpService otpService;
    private Notification notification;
    private FailedCounter failedCounter;
    private Logger logger;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        profile = Mockito.mock(Profile.class);
        hash = Mockito.mock(Hash.class);
        otpService = Mockito.mock(OtpService.class);
        notification = Mockito.mock(Notification.class);
        failedCounter = Mockito.mock(FailedCounter.class);
        logger = Mockito.mock(Logger.class);

        authentication = new AuthenticationService(profile, hash, otpService, failedCounter);
        authentication = new FailedCounterDecorator(authentication, failedCounter);
        authentication = new LogDecorator(authentication, failedCounter, logger);
        authentication = new NotificationDecorator(authentication, notification);
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

    @Test
    void reset_failed_count_when_valid() {
        // arrange
        whenValid();
        // assert
        shouldResetFailedCount(defaultAccount);
    }

    @Test
    void add_failed_count_when_invalid() {
        // arrange
        whenInvalid();
        // assert
        shouldAddFailedCount(defaultAccount);
    }

    @Test
    void log_failed_count_when_invalid() {
        // arrange
        givenFailedCount(defaultAccount, defaultFailedCount);
        whenInvalid();
        // assert
        logShouldContains(defaultAccount, defaultFailedCount);
    }

    @Test
    void notify_user_when_invalid() {
        // arrange
        whenInvalid();
        // assert
        shouldNotify(defaultAccount);
    }

    @Test
    void account_is_locked() {
        // arrange
        givenAccountIsLocked(defaultAccount, true);
        // assert
        shouldThrowAuthenticationException();
    }

    private void shouldThrowAuthenticationException() {
        Assertions.assertThatThrownBy(() -> authentication.isValid(defaultAccount, "password", "123456"))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining(defaultAccount);
    }

    private void givenAccountIsLocked(String account, boolean isLocked) {
        Mockito.when(failedCounter.isLocked(account)).thenReturn(isLocked);
    }

    private void shouldNotify(String account) {
        Mockito.verify(notification, Mockito.times(1)).notify(account, "account: " + account + " failed to login.");
    }

    private void logShouldContains(String account, int failedCount) {
        ArgumentCaptor<String> accountArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> failedCountArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(logger).info((String) ArgumentMatchers.any(), accountArgumentCaptor.capture(), failedCountArgumentCaptor.capture());
        Assertions.assertThat(accountArgumentCaptor.getValue()).isEqualTo(account);
        Assertions.assertThat(failedCountArgumentCaptor.getValue()).isEqualTo(failedCount);
    }

    private void givenFailedCount(String account, int failedCount) {
        Mockito.when(failedCounter.getFailedCount(account)).thenReturn(failedCount);
    }

    private void shouldAddFailedCount(String account) {
        Mockito.verify(failedCounter, Mockito.times(1)).increase(account);
    }

    private void whenInvalid() {
        // arrange
        givenPasswordFromProfile(defaultAccount, "my hashed password");
        givenHashedPassword(defaultAccount, "password", "my hashed password");
        givenOtp(defaultAccount, "123456");

        // act
        verify(defaultAccount, "password", "wrong otp");
    }

    private void shouldResetFailedCount(String account) {
        Mockito.verify(failedCounter, Mockito.times(1)).reset(account);
    }

    private void whenValid() {
        // arrange
        givenPasswordFromProfile(defaultAccount, "my hashed password");
        givenHashedPassword(defaultAccount, "password", "my hashed password");
        givenOtp(defaultAccount, "123456");

        // act
        verify(defaultAccount, "password", "123456");
    }

    private void shouldBeInvalid(boolean isValid) {
        Assertions.assertThat(isValid).isFalse();
    }

    private void shouldBeValid(boolean isValid) {
        Assertions.assertThat(isValid).isTrue();
    }

    private boolean verify(String account, String password, String otp) {
        return authentication.isValid(account, password, otp);
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
