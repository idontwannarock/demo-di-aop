package com.example.demodiaop.adapter.dao.repo;

import com.example.demodiaop.service.authentication.exception.AuthenticationException;
import com.example.demodiaop.service.authentication.gateway.Profile;

public class ProfileRepo implements Profile {

    private final AccountRepository accountRepository;

    public ProfileRepo(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public String getPassword(String account) {
        return accountRepository.findByUsername(account)
                .orElseThrow(() -> new AuthenticationException("query database for password error, account: " + account))
                .getPassword();
    }
}