package com.tech.ian.ticket.repository;

import com.tech.ian.ticket.dto.SectionsResponse;
import com.tech.ian.ticket.dto.TicketRequest;
import com.tech.ian.ticket.model.ConcertModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConcertRepository extends JpaRepository<ConcertModel, Long> {

    @Modifying
    @Query(value = "update sections_tb s set quantity = quantity - :#{#ticket.quantity()} from concert_tb c where c.id = s.concert_id and c.name = ?1 and s.section = :#{#ticket.section()} and s.quantity >= :#{#ticket.quantity()}", nativeQuery = true)
    void buyTickets(String concertName, TicketRequest ticket);

    @Query(value = "select s.id, s.section, s.quantity from concert_tb c JOIN sections_tb s ON s.concert_id = c.id where c.name IS NOT NULL and c.name = ?1  and s.quantity > 0 ORDER BY s.id", nativeQuery = true)
    List<SectionsResponse> findAllSections(String concertName);

    @Query(value = "select s.id, s.section, s.quantity from concert_tb c JOIN sections_tb s ON c.id = s.concert_id where c.name = ?1 and s.section = ?2 and s.quantity > 0", nativeQuery = true)
    Optional<SectionsResponse> findSection(String concertName, String section);

    Optional<ConcertModel> findByName(String name);
}
