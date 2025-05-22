package org.example.mails.events.repository;

import org.example.mails.events.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("""
                SELECT ep.event FROM EventPerson ep
                WHERE ep.person.email = :email
                  AND ep.responseStatus <> 'DECLINED'
                  AND ep.event.start BETWEEN :startOfDay AND :endOfDay
            """)
    List<Event> findAcceptedEventsByPersonEmailForToday(@Param("email") String email,
                                                        @Param("startOfDay") LocalDateTime startOfDay,
                                                        @Param("endOfDay") LocalDateTime endOfDay);

    @Query("""
                SELECT COUNT(e) FROM Event e
                WHERE e.start < :now
                  AND EXISTS (
                      SELECT 1 FROM EventPerson ep1
                      WHERE ep1.event = e AND ep1.person.id = :representativeId AND ep1.responseStatus <> 'DECLINED'
                  )
                  AND EXISTS (
                      SELECT 1 FROM EventPerson ep2
                      WHERE ep2.event = e AND ep2.person.id = :personId AND ep2.responseStatus <> 'DECLINED'
                  )
            """)
    int countPastMeetingsBetweenPeople(@Param("personAId") Long representativeId,
                                       @Param("personBId") Long personId,
                                       @Param("now") LocalDateTime now);
}
