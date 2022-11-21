package com.example.demodiaop.service.authentication.gateway;

public interface FailedCounter {
    void increase(String account);

    void reset(String account);

    boolean isLocked(String account);

    int getFailedCount(String account);
}
