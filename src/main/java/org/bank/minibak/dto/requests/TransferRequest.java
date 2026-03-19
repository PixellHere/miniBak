package org.bank.minibak.dto.requests;

import java.math.BigDecimal;

public record TransferRequest(
        String recipientTag,
        BigDecimal amount,
        String description,
        String attachment
) {}
