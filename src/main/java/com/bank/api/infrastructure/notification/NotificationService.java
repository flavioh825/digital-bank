package com.bank.api.infrastructure.notification;

import com.bank.api.domain.event.TransferCompletedEvent;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificationService {
    @Async
    @Retryable(
            retryFor = { Exception.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onTransferCompleted(TransferCompletedEvent event) {
        // Simula chamada a API para notificação (SMS, E-mail...)
        System.out.println("NOTIFICAÇÃO: " + event.ownerName() + " você recebeu R$ " + event.amount());

        /*
        * RETRIABLE: descomente a linha abaixo para testar, envie uma transação
        * e veja as tentativas no console até a falha
        */
        // throw new RuntimeException("Simulando falha ao chamar a API de notificação");
    }

    @Recover
    public void recover(Exception e, TransferCompletedEvent event) {
        System.err.println("FALHA CRÍTICA: Notificação não enviada para " + event.ownerName() + " após retentativas");
    }
}
