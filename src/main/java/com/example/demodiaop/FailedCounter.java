package com.example.demodiaop;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class FailedCounter {

    void increase(String account) {
        try {
            HttpRequest addFailCounterRequest = HttpRequest.newBuilder().uri(URI.create("https://example.com/fail-counter/add")).POST(HttpRequest.BodyPublishers.ofString(account, StandardCharsets.UTF_8)).build();
            HttpResponse<String> addFailCounterResponse = HttpClient.newHttpClient().send(addFailCounterRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (addFailCounterResponse.statusCode() != 200) {
                throw new AuthenticationException("add fail counter web api error, account: " + account);
            }
        } catch (IOException | InterruptedException e) {
            throw new AuthenticationException("add fail counter web api error, account: " + account, e);
        }
    }

    void reset(String account) {
        try {
            HttpRequest resetFailCounterRequest = HttpRequest.newBuilder().uri(URI.create("https://example.com/fail-counter/reset")).POST(HttpRequest.BodyPublishers.ofString(account, StandardCharsets.UTF_8)).build();
            HttpResponse<String> resetFailCounterResponse = HttpClient.newHttpClient().send(resetFailCounterRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (resetFailCounterResponse.statusCode() != 200) {
                throw new AuthenticationException("reset fail counter web api error, account: " + account);
            }
        } catch (IOException | InterruptedException e) {
            throw new AuthenticationException("reset fail counter web api error, account: " + account, e);
        }
    }

    boolean isLocked(String account) {
        boolean isLocked;
        try {
            HttpRequest isLockedRequest = HttpRequest.newBuilder().uri(URI.create("https://example.com/fail-counter/is-locked")).GET().build();
            HttpResponse<String> isLockedResponse = HttpClient.newHttpClient().send(isLockedRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (isLockedResponse.statusCode() != 200) {
                throw new AuthenticationException("check account is locked web api error, account: " + account);
            } else {
                isLocked = Boolean.parseBoolean(isLockedResponse.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new AuthenticationException("check account is locked web api error, account: " + account, e);
        }
        return isLocked;
    }

    int getFailedCount(String account) {
        int failedCount;
        try {
            HttpRequest getFailCountRequest = HttpRequest.newBuilder().uri(URI.create("https://example.com/fail-counter")).GET().build();
            HttpResponse<String> getFailCountResponse = HttpClient.newHttpClient().send(getFailCountRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (getFailCountResponse.statusCode() != 200) {
                throw new AuthenticationException("get fail counter web api error, account: " + account);
            } else {
                failedCount = Integer.parseInt(getFailCountResponse.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new AuthenticationException(e);
        }
        return failedCount;
    }
}