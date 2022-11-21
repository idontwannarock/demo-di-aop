package com.example.demodiaop.service.authentication;

public interface Authentication {
    boolean isValid(String account, String password, String otp);
}
