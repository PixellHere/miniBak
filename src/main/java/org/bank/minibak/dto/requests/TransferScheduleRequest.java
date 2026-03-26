package org.bank.minibak.dto.requests;

import java.time.LocalDate;
import java.util.UUID;

public record TransferScheduleRequest(
        UUID transferUuid,
        LocalDate scheduleTime,
        boolean blockFunds
) {
}
