package com.kztp.testengine.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public final class TokenService {
    private final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final int SECURE_TOKEN_LENGTH = 36;

    private final SecureRandom random = new SecureRandom();

    private final char[] symbols = CHARACTERS.toCharArray();

    private final char[] buf = new char[SECURE_TOKEN_LENGTH];

    public String generateToken() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }
}

