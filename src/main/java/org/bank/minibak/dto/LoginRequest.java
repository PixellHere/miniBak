package org.bank.minibak.dto;

public record LoginRequest(
        String email,
        String password
) {}
