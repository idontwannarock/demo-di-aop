package com.example.demodiaop.adapter.http;

import com.example.demodiaop.service.authentication.exception.AuthenticationException;
import com.example.demodiaop.service.authentication.gateway.FailedCounter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class FailedCounterImpl implements FailedCounter {

    private static final String ADD_FAIL_COUNTER_ACTION = "add fail counter";
    private static final String RESET_FAIL_COUNTER_ACTION = "reset fail counter";
    private static final String CHECK_ACCOUNT_IS_LOCKED_ACTION = "check account is locked";
    private static final String GET_FAIL_COUNTER_ACTION = "get fail counter";

    @Override
    public void increase(String account) {
        try {
            HttpRequest addFailCounterRequest = HttpRequest.newBuilder().uri(URI.create("https://example.com/fail-counter/add")).POST(HttpRequest.BodyPublishers.ofString(account, StandardCharsets.UTF_8)).build();
            HttpResponse<String> addFailCounterResponse = HttpClient.newHttpClient().send(addFailCounterRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (addFailCounterResponse.statusCode() != 200) {
                throw new AuthenticationException(getMessage(account, ADD_FAIL_COUNTER_ACTION));
            }
        } catch (IOException e) {
            throw new AuthenticationException(getMessage(account, ADD_FAIL_COUNTER_ACTION), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AuthenticationException(getMessage(account, ADD_FAIL_COUNTER_ACTION), e);
        }
    }

    @Override
    public void reset(String account) {
        try {
            HttpRequest resetFailCounterRequest = HttpRequest.newBuilder().uri(URI.create("https://example.com/fail-counter/reset")).POST(HttpRequest.BodyPublishers.ofString(account, StandardCharsets.UTF_8)).build();
            HttpResponse<String> resetFailCounterResponse = HttpClient.newHttpClient().send(resetFailCounterRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (resetFailCounterResponse.statusCode() != 200) {
                throw new AuthenticationException(getMessage(account, RESET_FAIL_COUNTER_ACTION));
            }
        } catch (IOException e) {
            throw new AuthenticationException(getMessage(account, RESET_FAIL_COUNTER_ACTION), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AuthenticationException(getMessage(account, RESET_FAIL_COUNTER_ACTION), e);
        }
    }

    @Override
    public boolean isLocked(String account) {
        boolean isLocked;
        try {
            HttpRequest isLockedRequest = HttpRequest.newBuilder().uri(URI.create("https://example.com/fail-counter/is-locked")).GET().build();
            HttpResponse<String> isLockedResponse = HttpClient.newHttpClient().send(isLockedRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (isLockedResponse.statusCode() != 200) {
                throw new AuthenticationException(getMessage(account, CHECK_ACCOUNT_IS_LOCKED_ACTION));
            } else {
                isLocked = Boolean.parseBoolean(isLockedResponse.body());
            }
        } catch (IOException e) {
            throw new AuthenticationException(getMessage(account, CHECK_ACCOUNT_IS_LOCKED_ACTION), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AuthenticationException(getMessage(account, CHECK_ACCOUNT_IS_LOCKED_ACTION), e);
        }
        return isLocked;
    }

    @Override
    public int getFailedCount(String account) {
        int failedCount;
        try {
            HttpRequest getFailCountRequest = HttpRequest.newBuilder().uri(URI.create("https://example.com/fail-counter")).GET().build();
            HttpResponse<String> getFailCountResponse = HttpClient.newHttpClient().send(getFailCountRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (getFailCountResponse.statusCode() != 200) {
                throw new AuthenticationException(getMessage(account, GET_FAIL_COUNTER_ACTION));
            } else {
                failedCount = Integer.parseInt(getFailCountResponse.body());
            }
        } catch (IOException e) {
            throw new AuthenticationException(getMessage(account, GET_FAIL_COUNTER_ACTION), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AuthenticationException(getMessage(account, GET_FAIL_COUNTER_ACTION), e);
        }
        return failedCount;
    }

    private static String getMessage(String account, String action) {
        return action + " web api error, account: " + account;
    }
}