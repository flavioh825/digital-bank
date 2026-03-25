package com.bank.api.domain.service;

import com.bank.api.domain.event.TransferCompletedEvent;
import com.bank.api.domain.exception.BusinessException;
import com.bank.api.domain.model.Account;
import com.bank.api.infrastructure.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final AccountRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void execute(Long from, Long to, BigDecimal amount) {
        Account source = repository.findById(from)
                .orElseThrow(() -> new BusinessException("Conta de origem não encontrada"));

        Account target = repository.findById(to)
                .orElseThrow(() -> new BusinessException("Conta de destino não encontrada"));

        source.debit(amount);
        target.credit(amount);

        repository.save(source);
        repository.save(target);

        eventPublisher.publishEvent(new TransferCompletedEvent(target.getOwnerName(), amount));
    }
}
