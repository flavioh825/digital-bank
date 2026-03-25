package com.bank.api.controller;

import com.bank.api.controller.response.AccountResponse;
import com.bank.api.domain.model.Account;
import com.bank.api.domain.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Contas", description = "Gerenciamento de contas corrente")
public class AccountController {
    private final AccountService service;

    @GetMapping
    @Operation(summary = "Lista contas do banco")
    public ResponseEntity<Page<AccountResponse>> list(
            @RequestParam(required = false) Long accountNumber,
            @ParameterObject Pageable pageable) {

        Page<Account> accounts = service.listAll(accountNumber, pageable);

        Page<AccountResponse> response = accounts.map(AccountResponse::fromEntity);

        return ResponseEntity.ok(response);
    }
}
