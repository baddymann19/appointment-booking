package org.enpal.appointmentbooking.repo;

import org.enpal.appointmentbooking.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Integer> {

    List<Slot> findByBookedTrueAndStartDateBetween(OffsetDateTime start, OffsetDateTime end);
    List<Slot> findByBookedFalseAndStartDateBetween(OffsetDateTime start, OffsetDateTime end);
}