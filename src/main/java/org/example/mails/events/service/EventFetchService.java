package org.example.mails.events.service;

import lombok.AllArgsConstructor;
import org.example.mails.events.model.EventFetchState;
import org.example.mails.events.repository.EventFetchRepository;
import org.example.mails.person.model.Person;
import org.example.mails.person.service.PersonService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class EventFetchService {
    private final EventFetchRepository eventFetchRepository;
    private final PersonService personService;

    public void saveLastUpdatedEventTime(LocalDateTime lastUpdatedEventTime, String email) {
        Person person = personService.getByEmail(email);
        EventFetchState newState = new EventFetchState(lastUpdatedEventTime, LocalDateTime.now(), person);
        eventFetchRepository.save(newState);
    }
}
