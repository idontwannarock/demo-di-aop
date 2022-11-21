package com.example.demodiaop;

public interface Authentication {
    boolean isValid(String account, String password, String otp);
}
