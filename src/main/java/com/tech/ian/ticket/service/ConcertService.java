package com.tech.ian.ticket.service;

import com.tech.ian.ticket.dto.ConcertCreateRequest;
import com.tech.ian.ticket.dto.SectionsResponse;
import com.tech.ian.ticket.dto.TicketRequest;
import com.tech.ian.ticket.model.ConcertModel;
import com.tech.ian.ticket.model.SectionsModel;
import com.tech.ian.ticket.repository.ConcertRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConcertService {

    private final ConcertRepository concertRepository;

    public ConcertService(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    public void create(ConcertCreateRequest request) {
        concertRepository.findByName(request.concertName()).ifPresent(e -> {
           throw new RuntimeException();
        });

        List<SectionsModel> list = request.sectionsList().stream()
                .map(sectionsCreateRequest ->
                        SectionsModel.builder()
                                .section(sectionsCreateRequest.section())
                                .quantity(sectionsCreateRequest.quantity())
                                .build()
                ).toList();

        ConcertModel concert = ConcertModel.builder()
                .name(request.concertName())
                .singer(request.singerName())
                .sectionsAvailable(list)
                .build();

        list.forEach(sectionsModel -> sectionsModel.setConcert(concert));

        concertRepository.save(concert);
    }

    public List<SectionsResponse> findAllSectionFor(String concert) {
        List<SectionsResponse> allSections = concertRepository.findAllSections(concert);
        if (allSections.isEmpty()) throw new RuntimeException();
        return allSections;
    }

    @Transactional()
    public void buyTickets(String concert, TicketRequest request) {
        findSection(concert, request.section()).orElseThrow(RuntimeException::new);
        concertRepository.buyTickets(concert, request);
    }

    private Optional<SectionsResponse> findSection(String concert, String section) {
        return concertRepository.findSection(concert, section);
    }
}
