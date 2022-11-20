package com.example.demodiaop;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class OtpServiceImpl implements OtpService {

    @Override
    public String getCurrentOtp(String account) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create("https://example.com/otp")).GET().build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new AuthenticationException("get current OTP web api error, account: " + account);
            }
        } catch (IOException | InterruptedException e) {
            throw new AuthenticationException("get current OTP web api error, account: " + account, e);
        }
    }
}