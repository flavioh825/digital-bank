package com.bank.api.infrastructure.repository;

import com.bank.api.domain.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findAllByAccountIdOrderByCreatedAtDesc(Long accountId, Pageable pageable);

    boolean existsByCorrelationId(String correlationId);
}
