package com.example.demodiaop;

public interface FailedCounter {
    void increase(String account);

    void reset(String account);

    boolean isLocked(String account);

    int getFailedCount(String account);
}
