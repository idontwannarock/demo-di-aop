package com.example.demodiaop.service.authentication;

import com.example.demodiaop.service.authentication.gateway.Hash;
import com.example.demodiaop.service.authentication.gateway.OtpService;
import com.example.demodiaop.service.authentication.gateway.Profile;

public class AuthenticationService implements Authentication {

    private final Profile profile;
    private final Hash hash;
    private final OtpService otpService;

    public AuthenticationService(Profile profile, Hash hash, OtpService otpService) {
        this.profile = profile;
        this.hash = hash;
        this.otpService = otpService;
    }

    @Override
    public boolean isValid(String account, String password, String otp) {
        String passwordFromDb = profile.getPassword(account);
        String hashedPassword = hash.compute(account, password);
        String currentOtp = otpService.getCurrentOtp(account);

        return hashedPassword.equals(passwordFromDb) && currentOtp.equals(otp);
    }

}