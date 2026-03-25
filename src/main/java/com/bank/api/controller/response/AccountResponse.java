package com.bank.api.controller.response;

import com.bank.api.domain.model.Account;

import java.math.BigDecimal;

public record AccountResponse(
        Long accountNumber,
        String ownerName,
        BigDecimal balance
) {
    public static AccountResponse fromEntity(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getOwnerName(),
                account.getBalance()
        );
    }
}
