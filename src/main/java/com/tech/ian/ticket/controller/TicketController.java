package com.tech.ian.ticket.controller;

import com.tech.ian.ticket.dto.*;
import com.tech.ian.ticket.model.SectionsModel;
import com.tech.ian.ticket.service.ConcertService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TicketController {

    private final ConcertService service;

    public TicketController(ConcertService service) {
        this.service = service;
    }

    @PostMapping
    public void create(@RequestBody ConcertCreateRequest request) {
        service.create(request);
    }

    @GetMapping("{concertName}")
    public ResponseEntity<List<SectionsResponse>> findAllSections(@PathVariable String concertName) {

        return new ResponseEntity<>(service.findAllSectionFor(concertName), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<TicketResponse> buyTicket(@RequestBody CreateTicketRequest request) {
        service.buyTickets(request.concert(), request.ticket());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
