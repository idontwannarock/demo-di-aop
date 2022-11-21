package com.example.demodiaop;

public abstract class AuthenticationDecoratorBase implements Authentication {

    protected final Authentication authentication;

    protected AuthenticationDecoratorBase(Authentication authentication) {
        this.authentication = authentication;
    }
}
