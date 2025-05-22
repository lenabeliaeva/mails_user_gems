package org.example.mails.emails.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mails.events.model.Event;
import org.example.mails.person.model.Person;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    public Email(String emailContent, Person recipient, Event event) {
        this.emailContent = emailContent;
        this.recipient = recipient;
        this.event = event;
    }

    private LocalDateTime sentAt;

    // should be handled different
    private String emailContent;

    @ManyToOne
    private Person recipient;

    @ManyToOne
    private Event event;
}
