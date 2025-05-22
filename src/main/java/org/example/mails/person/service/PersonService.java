package org.example.mails.person.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.example.mails.person.dto.PersonDto;
import org.example.mails.person.model.Person;
import org.example.mails.person.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@CommonsLog
@AllArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonApiClient personApiClient;
    private final ObjectMapper objectMapper;

    public Person getByEmail(String email) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        Optional<Person> optionalPerson = personRepository.findByEmail(email);
        if (optionalPerson.isPresent() && optionalPerson.get().getLastUpdated().isAfter(thirtyDaysAgo)) {
            return optionalPerson.get();
        } else {
            try {
                PersonDto newPerson = personApiClient.fetchNewPerson(email);
                return save(newPerson);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            log.warn("No person found with email: " + email);
            return null;
        }
    }

    public Person save(PersonDto newPerson) {
        Person person = objectMapper.convertValue(newPerson, Person.class);
        return personRepository.save(person);
    }
}
