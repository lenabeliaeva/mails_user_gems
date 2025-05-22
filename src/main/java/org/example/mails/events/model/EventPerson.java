package org.example.mails.events.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mails.person.model.Person;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class EventPerson {
    @EmbeddedId
    private EventPersonId id;

    @ManyToOne
    @MapsId("eventId")
    private Event event;

    @ManyToOne
    @MapsId("personId")
    private Person person;

    @Enumerated(EnumType.STRING)
    private ResponseStatus responseStatus;

    public EventPerson(Event event, Person person, ResponseStatus responseStatus) {
        this.event = event;
        this.person = person;
        this.responseStatus = responseStatus;
    }
}
