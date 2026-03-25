package com.bank.api.domain.service;

import com.bank.api.domain.event.TransferCompletedEvent;
import com.bank.api.domain.exception.BusinessException;
import com.bank.api.domain.model.Account;
import com.bank.api.domain.model.Transaction;
import com.bank.api.infrastructure.repository.AccountRepository;
import com.bank.api.infrastructure.repository.TransactionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private TransferService service;

    @Test
    @DisplayName("Deve realizar uma transferência com sucesso quando os dados forem válidos")
    void executeTransferSuccess() {
        var sourceId = 1L;
        var targetId = 2L;

        Account source = new Account(sourceId, "Ana", BigDecimal.valueOf(100), 0L);
        Account target = new Account(targetId, "Fernando", BigDecimal.valueOf(50), 0L);
        BigDecimal amount = BigDecimal.valueOf(30);

        when(accountRepository.findById(sourceId)).thenReturn(Optional.of(source));
        when(accountRepository.findById(targetId)).thenReturn(Optional.of(target));

        service.execute(sourceId, targetId, amount);

        assertEquals(BigDecimal.valueOf(70), source.getBalance());
        assertEquals(BigDecimal.valueOf(80), target.getBalance());

        verify(accountRepository, times(1)).save(source);
        verify(accountRepository, times(1)).save(target);
        verify(transactionRepository, times(2)).save(any(Transaction.class));

        // testa envio do evento
        verify(publisher, times(1))
                .publishEvent(new TransferCompletedEvent(target.getOwnerName(), amount));
    }

    @Test
    @DisplayName("Deve lançar exceção quando saldo for insuficiente")
    void shouldThrowExceptionWhenBalanceIsInsufficient() {
        var sourceId = 1L;
        var targetId = 2L;

        Account source = new Account(sourceId, "Ana", BigDecimal.valueOf(10), 0L);
        Account target = new Account(targetId, "Fernando", BigDecimal.valueOf(50), 0L);

        when(accountRepository.findById(sourceId)).thenReturn(Optional.of(source));
        when(accountRepository.findById(targetId)).thenReturn(Optional.of(target));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                service.execute(sourceId, targetId, BigDecimal.valueOf(100)));

        Assertions.assertThat(ex.getMessage()).isEqualTo("Saldo insuficiente");

        // save não deve ser chamado
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando valor de transferência for negativo")
    void shouldThrowExceptionWhenTransferAmountIsNegative() {
        var sourceId = 1L;
        var targetId = 2L;

        Account source = new Account(sourceId, "Ana", BigDecimal.valueOf(100), 0L);
        Account target = new Account(targetId, "Fernando", BigDecimal.valueOf(500), 0L);

        when(accountRepository.findById(sourceId)).thenReturn(Optional.of(source));
        when(accountRepository.findById(targetId)).thenReturn(Optional.of(target));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                service.execute(sourceId, targetId, BigDecimal.valueOf(-10)));

        Assertions.assertThat(ex.getMessage())
                .isEqualTo("Somente valores positivos são aceitos para esta ação");

        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando não encontrar a conta de origem")
    void shouldThrowExceptionWhenSourceAccountNotExists() {
        var sourceId = 1L;
        var targetId = 2L;

        when(accountRepository.findById(sourceId)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () ->
                service.execute(sourceId, targetId, BigDecimal.valueOf(10)));

        Assertions.assertThat(ex.getMessage())
                .isEqualTo("Conta de origem não encontrada");

        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando não encontrar a conta de destino")
    void shouldThrowExceptionWhenTargetAccountNotExists() {
        var sourceId = 1L;
        var targetId = 2L;

        Account source = new Account(sourceId, "Ana", BigDecimal.valueOf(100), 0L);
        when(accountRepository.findById(sourceId)).thenReturn(Optional.of(source));
        when(accountRepository.findById(targetId)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () ->
                service.execute(sourceId, targetId, BigDecimal.valueOf(10)));

        Assertions.assertThat(ex.getMessage())
                .isEqualTo("Conta de destino não encontrada");

        verify(accountRepository, never()).save(any());
    }
}
