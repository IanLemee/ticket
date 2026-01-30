package com.tech.ian.ticket.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Table(name = "CONCERT_TB")
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConcertModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String singer;

    @OneToMany(mappedBy = "concert",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private List<SectionsModel> sectionsAvailable;
}
