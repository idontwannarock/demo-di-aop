package com.example.demodiaop.service.authentication.gateway;

public interface Hash {
    String compute(String account, String password);
}
