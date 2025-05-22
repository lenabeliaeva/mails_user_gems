package org.example.mails.emails.dto;

import org.example.mails.person.dto.CompanyDto;
import org.example.mails.person.dto.PersonDto;

import java.time.LocalDateTime;
import java.util.List;

public record EmailContent(LocalDateTime start, LocalDateTime end, String title, String[] representatives,
                           CompanyDto company, List<PersonDto> accepted, List<PersonDto> rejected) {
}
