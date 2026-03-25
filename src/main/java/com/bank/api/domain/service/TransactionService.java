package com.bank.api.domain.service;

import com.bank.api.domain.model.Transaction;
import com.bank.api.infrastructure.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repository;

    @Transactional(readOnly = true)
    public Page<Transaction> listByAccount(Long accountId, Pageable page) {
        return repository.findAllByAccountIdOrderByCreatedAtDesc(accountId, page);
    }
}
