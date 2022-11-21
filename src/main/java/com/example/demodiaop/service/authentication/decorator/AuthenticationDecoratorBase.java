package com.example.demodiaop.service.authentication.decorator;

import com.example.demodiaop.service.authentication.Authentication;

public abstract class AuthenticationDecoratorBase implements Authentication {

    protected final Authentication authentication;

    protected AuthenticationDecoratorBase(Authentication authentication) {
        this.authentication = authentication;
    }
}
