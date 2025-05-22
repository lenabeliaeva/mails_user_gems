package org.example.mails.emails.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.example.mails.config.ApiKeys;
import org.example.mails.emails.dto.EmailContent;
import org.example.mails.emails.model.Email;
import org.example.mails.emails.repository.EmailRepository;
import org.example.mails.events.model.Event;
import org.example.mails.events.model.EventPerson;
import org.example.mails.events.model.ResponseStatus;
import org.example.mails.events.service.EventService;
import org.example.mails.person.dto.CompanyDto;
import org.example.mails.person.dto.PersonDto;
import org.example.mails.person.model.Person;
import org.example.mails.person.service.PersonService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class EmailService {
    private static final String userGemsDomain = "@usergems.com";

    private final EmailRepository emailRepository;
    private final EventService eventService;
    private final ObjectMapper objectMapper;
    private final PersonService personService;

    @Scheduled(cron = "0 0 8 * * *")
    public void generateEmails() {
        List<String> representativeEmails = ApiKeys.getRepresentativeEmails();
        for (String email : representativeEmails) {
            Person recipient = personService.getByEmail(email);
            List<Event> events =  eventService.getEventsByRepresentativeEmailForToday(email);
            for (Event event : events) {
                EmailContent emailContent = collectDataForEnrichment(event);
                Email generatedEmail = new Email(emailContent.toString(), recipient, event);
                sendEmail(generatedEmail);
            }
        }
    }

    private void sendEmail(Email email) {
        if (email != null) {
            email.setSentAt(LocalDateTime.now());
            emailRepository.save(email);
        }
    }

    private EmailContent collectDataForEnrichment(Event event) {
        String[] representatives = event.getParticipants()
                .stream()
                .map(EventPerson::getPerson)
                .map(Person::getEmail)
                .filter(e -> e.endsWith(userGemsDomain))
                .toArray(String[]::new);

        List<PersonDto> accepted = event.getParticipants()
                .stream()
                .filter(eventPerson -> eventPerson.getResponseStatus() == ResponseStatus.ACCEPTED)
                .map(EventPerson::getPerson)
                .filter(e -> !e.getEmail().endsWith(userGemsDomain))
                .map(p -> objectMapper.convertValue(p, PersonDto.class))
                .toList();
        accepted.forEach(p -> p.setPastMeetingsCount(eventService.getPastMeetingsCount(p.getId())));

        List<PersonDto> rejected = event.getParticipants()
                .stream()
                .filter(eventPerson -> eventPerson.getResponseStatus() == ResponseStatus.REJECTED)
                .map(EventPerson::getPerson)
                .filter(e -> !e.getEmail().endsWith(userGemsDomain))
                .map(p -> objectMapper.convertValue(p, PersonDto.class))
                .toList();
        rejected.forEach(p -> p.setPastMeetingsCount(eventService.getPastMeetingsCount(p.getId())));

        CompanyDto companyDto = null;
        if (!accepted.isEmpty()) {
            companyDto = accepted
                    .stream()
                    .map(PersonDto::getCompany)
                    .findFirst()
                    .orElse(null);
        }

        return new EmailContent(event.getStart(), event.getEnd(), event.getTitle(), representatives,
                companyDto, accepted, rejected);
    }
}
