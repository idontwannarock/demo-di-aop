package com.example.demodiaop;

public class NotificationDecorator implements Authentication {

    private final Authentication authentication;
    private final Notification notification;

    public NotificationDecorator(Authentication authentication, Notification notification) {
        this.notification = notification;
        this.authentication = authentication;
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