package com.github.cyberxandrew.repository;

import com.github.cyberxandrew.model.Ticket;
import org.springframework.messaging.handler.annotation.Payload;

public interface TicketRepository {
    void consumeTicket(Ticket ticket);
    void returnTicket(Ticket ticket);
    Ticket save(Ticket ticket);
    void delete(Long ticketId);
}
