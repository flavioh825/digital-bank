package com.bank.api.controller;

import com.bank.api.controller.request.TransferRequest;
import com.bank.api.domain.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
@Tag(name = "Transferência", description = "Realiza movimentações bancárias")
public class TransferController {
    private final TransferService service;

    @PostMapping
    @Operation(summary = "Realiza transferências entre contas")
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferRequest request) {
        service.execute(request.fromAccountId(), request.toAccountId(), request.amount(), request.correlationId());
        return ResponseEntity.ok().build();
    }
}
