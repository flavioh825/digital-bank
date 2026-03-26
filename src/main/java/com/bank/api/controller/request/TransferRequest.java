package com.bank.api.controller.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull(message = "Conta de origem é obrigatória")
        @Positive(message = "Número da conta de origem é inválido")
        Long fromAccountId,

        @NotNull(message = "Conta destino é obrigatória")
        @Positive(message = "Número da conta de destino é inválido")
        Long toAccountId,

        @NotNull(message = "O valor da transferência é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        BigDecimal amount,

        @NotBlank(message = "correlationId is required")
        String correlationId
) {}
