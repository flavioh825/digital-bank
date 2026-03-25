package com.bank.api.domain.service;

import com.bank.api.domain.event.TransferCompletedEvent;
import com.bank.api.domain.exception.BusinessException;
import com.bank.api.domain.model.Account;
import com.bank.api.domain.model.Transaction;
import com.bank.api.infrastructure.repository.AccountRepository;
import com.bank.api.infrastructure.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final AccountRepository repository;
    private final TransactionRepository transactionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void execute(Long from, Long to, BigDecimal amount) {
        Account source = repository.findById(from)
                .orElseThrow(() -> new BusinessException("Conta de origem não encontrada"));

        Account target = repository.findById(to)
                .orElseThrow(() -> new BusinessException("Conta de destino não encontrada"));

        source.debit(amount);
        target.credit(amount);

        // Registra movimentações na transaction
        transactionRepository.save(new Transaction(
                null, source, amount.negate(), LocalDateTime.now(), "Transferência para " + target.getOwnerName()));
        transactionRepository.save(new Transaction(
                null, target, amount, LocalDateTime.now(), "Transferência recebida de " + source.getOwnerName()));

        repository.save(source);
        repository.save(target);

        // Publica evento de notificação
        eventPublisher.publishEvent(new TransferCompletedEvent(target.getOwnerName(), amount));
    }
}
