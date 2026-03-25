package com.bank.api.domain.service;

import com.bank.api.domain.model.Account;
import com.bank.api.infrastructure.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository repository;

    @Transactional(readOnly = true)
    public Page<Account> listAll(Long accountId, Pageable page) {
        return repository.findAllByIdOptional(accountId, page);
    }
}
