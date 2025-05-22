package org.example.mails.events.service;

import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.example.mails.config.ApiKeys;
import org.example.mails.events.dto.EventApiResponse;
import org.example.mails.events.model.Event;
import org.example.mails.events.model.EventFetchState;
import org.example.mails.events.model.EventPerson;
import org.example.mails.events.model.ResponseStatus;
import org.example.mails.events.repository.EventFetchRepository;
import org.example.mails.events.repository.EventRepository;
import org.example.mails.person.model.Person;
import org.example.mails.person.service.PersonService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@CommonsLog
public class EventService {
    private final EventRepository eventRepository;
    private final EventFetchRepository eventFetchRepository;
    private final EventApiClient eventApiService;
    private final EventFetchService eventFetchService;
    private final PersonService personService;

    public List<Event> getEventsByRepresentativeEmailForToday(String representativeEmail) {
        return eventRepository.findAcceptedEventsByPersonEmailForToday(representativeEmail,
                LocalDate.now().atStartOfDay(), LocalDate.now().atTime(LocalTime.MAX));
    }

    public Map<String, Integer> getPastMeetingsCount(Long personId) {
        Map<String, Integer> pastMeetingsCount = new HashMap<>();
        for(String email: ApiKeys.getRepresentativeEmails()) {
            Long representativeId = personService.getByEmail(email).getId();
            int count = eventRepository.countPastMeetingsBetweenPeople(representativeId, personId,
                    LocalDate.now().atStartOfDay());
            pastMeetingsCount.put(email, count);
        }
        return pastMeetingsCount;
    }

    @Scheduled(cron = "0 0 23 * * *")
    public void fetchNewEvents() {
        List<String> representativeEmails = ApiKeys.getRepresentativeEmails();
        representativeEmails.forEach(this::fetchNewEventsForRepresentative);
    }

    private void fetchNewEventsForRepresentative(String email) {
        LocalDateTime lastFetchTime = getLastFetchedEventTime(email);
        int pageNumber = 1;

        List<EventApiResponse> newEvents;

        LocalDateTime currentLastFetchTime = null;
        newEvents = getNextPageEvents(email, pageNumber++);
        while (!newEvents.isEmpty()) {
            for (EventApiResponse event : newEvents) {
                if (lastFetchTime != null && event.changed().isAfter(lastFetchTime)) {
                    saveNewEvent(event);
                    if (currentLastFetchTime == null) {
                        // save the first time because it is the newest
                        currentLastFetchTime = event.changed();
                    }
                } else {
                    eventFetchService.saveLastUpdatedEventTime(currentLastFetchTime, email);
                    break;
                }
            }
            newEvents = getNextPageEvents(email, pageNumber++);
        }
    }

    public void saveNewEvent(EventApiResponse newEvent) {
        Event entityEvent = dtoToEntity(newEvent);

        Set<EventPerson> eventPersonSet = new HashSet<>();

        Arrays.stream(newEvent.accepted()).forEach(e -> {
            Person person = personService.getByEmail(e);
            eventPersonSet.add(new EventPerson(entityEvent, person, ResponseStatus.ACCEPTED));
        });
        Arrays.stream(newEvent.rejected()).forEach(e -> {
            Person person = personService.getByEmail(e);
            eventPersonSet.add(new EventPerson(entityEvent, person, ResponseStatus.REJECTED));
        });


        entityEvent.setParticipants(eventPersonSet);

        eventRepository.save(entityEvent);
    }

    private List<EventApiResponse> getNextPageEvents(String email, int pageNumber) {
        try {
            return eventApiService.fetchNewEventsFromApi(email, pageNumber);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    private LocalDateTime getLastFetchedEventTime(String representativeEmail) {
        LocalDateTime lastFetchTime = null;
        Optional<EventFetchState> lastFetchedEvent = eventFetchRepository
                .findFirstByPerson_EmailOrderByLastFetchedEventDateDesc(representativeEmail);
        if (lastFetchedEvent.isPresent()) {
            lastFetchTime = lastFetchedEvent.get().getLastFetchedEventDate();
        }
        return lastFetchTime;
    }

    private Event dtoToEntity(EventApiResponse dto) {
        Event entityEvent = new Event();
        entityEvent.setEventId(dto.id());
        entityEvent.setStart(dto.start());
        entityEvent.setEnd(dto.end());
        entityEvent.setChanged(dto.changed());
        entityEvent.setTitle(dto.title());
        return entityEvent;
    }
}
