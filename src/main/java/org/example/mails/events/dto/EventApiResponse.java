package org.example.mails.events.dto;

import java.time.LocalDateTime;

public record EventApiResponse(Long id, LocalDateTime changed, LocalDateTime start, LocalDateTime end, String title,
                               String[] accepted, String[] rejected) {
}
