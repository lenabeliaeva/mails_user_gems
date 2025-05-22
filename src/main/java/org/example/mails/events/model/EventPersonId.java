package org.example.mails.events.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class EventPersonId implements Serializable {
    private Long eventId;
    private Long personId;
}
