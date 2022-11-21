package com.example.demodiaop.service.authentication.decorator;

import com.example.demodiaop.service.authentication.Authentication;
import com.example.demodiaop.service.authentication.gateway.Notification;

public class NotificationDecorator extends AuthenticationDecoratorBase {

    private final Notification notification;

    public NotificationDecorator(Authentication authentication, Notification notification) {
        super(authentication);
        this.notification = notification;
    }

    @Override
    public boolean isValid(String account, String password, String otp) {
        boolean isValid = authentication.isValid(account, password, otp);
        if (!isValid) {
            notify(account);
        }
        return isValid;
    }

    private void notify(String account) {
        notification.notify(account, "account: " + account + " failed to login.");
    }
}