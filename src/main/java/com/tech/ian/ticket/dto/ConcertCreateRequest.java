package com.tech.ian.ticket.dto;

import java.util.List;

public record ConcertCreateRequest(String concertName, String singerName, List<SectionsCreateRequest> sectionsList) {
}
