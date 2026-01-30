package com.tech.ian.ticket.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "SECTIONS_TB")
@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String section;
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private ConcertModel concert;
}