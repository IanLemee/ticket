package com.tech.ian.ticket.dto;

public record CreateTicketRequest(String concert,
                                  TicketRequest ticket) {
}
