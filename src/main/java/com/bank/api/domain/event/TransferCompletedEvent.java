package com.bank.api.domain.event;

import java.math.BigDecimal;

public record TransferCompletedEvent(
        String ownerName,
        BigDecimal amount
) {}
