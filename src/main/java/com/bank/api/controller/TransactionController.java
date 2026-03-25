package com.bank.api.controller;

import com.bank.api.controller.response.TransactionResponse;
import com.bank.api.domain.model.Transaction;
import com.bank.api.domain.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Movimentações", description = "Histórico de movimentações bancárias")
public class TransactionController {
    private final TransactionService service;

    @GetMapping("/{accountId}/statement")
    @Operation(summary = "Resgata extrato de uma conta bancária")
    public ResponseEntity<Page<TransactionResponse>> getStatement(
            @PathVariable Long accountId,
            @ParameterObject Pageable pageable) {
        Page<Transaction> transactions = service.listByAccount(accountId, pageable);
        return ResponseEntity.ok(transactions.map(TransactionResponse::fromEntity));
    }
}
