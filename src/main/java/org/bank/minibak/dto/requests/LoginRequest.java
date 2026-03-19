package org.bank.minibak.dto.requests;

public record LoginRequest(
        String email,
        String password
) {}
