package org.example.mails.events.repository;

import org.example.mails.events.model.EventFetchState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventFetchRepository extends JpaRepository<EventFetchState, Long> {
    Optional<EventFetchState> findFirstByPerson_EmailOrderByLastFetchedEventDateDesc(String email);
}
