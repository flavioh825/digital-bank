package com.bank.api.domain.service;

import com.bank.api.domain.model.Account;
import com.bank.api.infrastructure.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountService service;

    @Test
    @DisplayName("Deve repassar os parâmetros de filtro e paginação corretamente para o repository")
    void shouldPassCorrectParametersToRepository() {
        Long accountId = 123L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Account> expectedPage = new PageImpl<>(List.of(new Account()));

        when(repository.findAllByIdOptional(accountId, pageable)).thenReturn(expectedPage);

        Page<Account> result = service.listAll(accountId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        verify(repository).findAllByIdOptional(accountId, pageable);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando o repository não encontrar nada")
    void shouldReturnEmptyPageWhenNoAccountsFound() {
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAllByIdOptional(any(), eq(pageable))).thenReturn(Page.empty());

        Page<Account> result = service.listAll(null, pageable);

        assertTrue(result.isEmpty());
        verify(repository).findAllByIdOptional(null, pageable);
    }
}
