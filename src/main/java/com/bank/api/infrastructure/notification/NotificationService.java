package com.bank.api.infrastructure.notification;

import com.bank.api.domain.event.TransferCompletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificationService {
    @Async
    @EventListener
    public void onTransferCompleted(TransferCompletedEvent event) {
        // Simula chamada a API para notificação (SMS, E-mail...)
        System.out.println("NOTIFICAÇÃO: " + event.ownerName() + " você recebeu R$ " + event.amount());
    }
}
