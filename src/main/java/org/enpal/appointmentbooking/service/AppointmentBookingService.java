package org.enpal.appointmentbooking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.enpal.appointmentbooking.dto.AvailableSlotDto;
import org.enpal.appointmentbooking.dto.SlotRequestDto;
import org.enpal.appointmentbooking.entity.SalesManager;
import org.enpal.appointmentbooking.entity.Slot;
import org.enpal.appointmentbooking.repo.SlotRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentBookingService {

    private final SlotRepository slotRepository;

    @Transactional
    public List<AvailableSlotDto> findAvailableSlots(SlotRequestDto requestDto) {
        OffsetDateTime startOfDay = requestDto.getDate().atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        OffsetDateTime endOfDay = startOfDay.plusDays(1);

        List<Slot> allSlots = slotRepository.findByBookedFalseAndStartDateBetween(startOfDay, endOfDay);
        if(allSlots.isEmpty()) {
            return Collections.emptyList();
        }

        List<Slot> bookedSlots = slotRepository.findByBookedTrueAndStartDateBetween(startOfDay, endOfDay);

        Map<Integer, List<Slot>> bookedSlotsMap = groupSlotsBySalesManager(bookedSlots);

        return allSlots.stream()
                .filter(slot -> isSlotMatching(slot, requestDto))
                .filter(slot -> isSlotAvailable(slot, bookedSlotsMap.get(slot.getSalesManager().getId())))
                .collect(Collectors.groupingBy(Slot::getStartDate, Collectors.counting()))
                .entrySet().stream()
                .map(entry -> new AvailableSlotDto(entry.getKey(), entry.getValue().intValue()))
                .sorted(Comparator.comparing(AvailableSlotDto::getStartDate))
                .toList();
    }

    private Map<Integer, List<Slot>> groupSlotsBySalesManager(List<Slot> slots) {
        return slots.stream().collect(Collectors.groupingBy(slot -> slot.getSalesManager().getId()));
    }

    private boolean isSlotAvailable(Slot availableSlot, List<Slot> bookedSlots) {
        return bookedSlots == null || bookedSlots.stream().noneMatch(bookedSlot -> isDateOverlapped(bookedSlot, availableSlot));
    }

    private boolean isDateOverlapped(Slot bookedSlot, Slot availableSlot) {
        return bookedSlot.getStartDate().isBefore(availableSlot.getEndDate()) &&
                bookedSlot.getEndDate().isAfter(availableSlot.getStartDate());
    }

    private boolean isSlotMatching(Slot slot, SlotRequestDto requestDto) {
        SalesManager manager = slot.getSalesManager();
        return manager.getLanguages().contains(requestDto.getLanguage()) &&
                manager.getProducts().containsAll(requestDto.getProducts()) &&
                manager.getCustomerRatings().contains(requestDto.getRating());
    }
}
