package org.example.mails.events.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.mails.person.model.Person;

import java.time.LocalDateTime;

@Table
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventFetchState {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    public EventFetchState(LocalDateTime lastFetchedEventDate, LocalDateTime updatedAt, Person person) {
        this.lastFetchedEventDate = lastFetchedEventDate;
        this.updatedAt = updatedAt;
        this.person = person;
    }

    private LocalDateTime lastFetchedEventDate;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    private Person person;
}
