package com.bank.api.controller.response;

import com.bank.api.domain.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
   Long id,
   Long accountId,
   LocalDateTime date,
   BigDecimal amount,
   String description
) {
    public static TransactionResponse fromEntity(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAccount().getId(),
                transaction.getCreatedAt(),
                transaction.getAmount(),
                transaction.getDescription()
        );
    }
}
