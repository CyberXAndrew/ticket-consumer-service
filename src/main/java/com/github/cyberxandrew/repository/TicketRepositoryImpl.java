package com.github.cyberxandrew.repository;

import com.github.cyberxandrew.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Repository;

@Repository
public class TicketRepositoryImpl implements TicketRepository {

    @Autowired JdbcTemplate jdbcTemplate;
    private final RowMapper<Ticket> ticketRowMapper = new BeanPropertyRowMapper<>(Ticket.class);
    private static final Logger logger = LoggerFactory.getLogger(TicketRepositoryImpl.class);

    @Override
    @KafkaListener(topics = "purchased-ticket", groupId = "ticket-consumers")
    public void consumeTicket(@Payload Ticket ticket) {
        save(ticket);
        logger.info("Ticket successfully consumed");
    }

    @Override
    @KafkaListener(topics = "returned-ticket", groupId = "ticket-consumers")
    public void returnTicket(@Payload Ticket ticket) {
        delete(ticket.getId());
        logger.info("Ticket successfully returned");
    }

    @Override
    public void delete(Long ticketId) {
        String sql = "DELETE FROM tickets WHERE id = ?";
        jdbcTemplate.update(sql, ticketId);
        logger.info("Ticket with id {} successfully deleted", ticketId);
    }

    @Override
    public Ticket save(Ticket ticket) {
        String sql = "INSERT INTO tickets (id, date_time, user_id, route_id, price, seat_number)" +
                " VALUES (?, ?, ?, ?, ?, ?)";

        int i = jdbcTemplate.update(sql, ticket.getId(), ticket.getDateTime(), ticket.getUserId(), ticket.getRouteId(),
                ticket.getPrice(), ticket.getSeatNumber());
        if (i > 0) logger.info("Ticket successfully saved");

        return ticket;
    }
}
