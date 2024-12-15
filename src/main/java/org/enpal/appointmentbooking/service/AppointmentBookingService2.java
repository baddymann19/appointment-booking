//package org.enpal.appointmentbooking.service;
//
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.enpal.appointmentbooking.dto.AvailableSlotDto;
//import org.enpal.appointmentbooking.dto.SlotRequestDto;
//import org.enpal.appointmentbooking.entity.SalesManager;
//import org.enpal.appointmentbooking.entity.Slot;
//import org.enpal.appointmentbooking.repo.SlotRepository;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.OffsetDateTime;
//import java.time.ZoneOffset;
//import java.util.Comparator;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class AppointmentBookingService2 {
//
//    private final SlotRepository slotRepository;
//    @Transactional
//    public List<AvailableSlotDto> findAvailableSlots(SlotRequestDto requestDto) {
//        // Parse the date from the request
//        LocalDate queryDate = requestDto.getDate();
//        OffsetDateTime startOfDay = queryDate.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
//        OffsetDateTime endOfDay = startOfDay.plusDays(1);
//
//        // Fetch all slots within the given date range
//        List<Slot> slots = slotRepository.findByBookedFalseAndStartDateBetween(startOfDay, endOfDay);
//
//        // Filter slots by sales manager criteria
//        List<Slot> filteredSlots = slots.stream()
//                .filter(slot ->
//                    isSlotMatching(slot, requestDto))
//                .toList();
//
//        List<Slot> bookedSlots = slotRepository.findByBookedTrueAndStartDateBetween(startOfDay, endOfDay);
//        // you can turn this to class object so you do not have to go into the DB all the time
//        Map<Integer, List<Slot>> bookedSlotsMap = bookedSlots.stream()
//                .collect(Collectors.groupingBy(slot -> slot.getSalesManager().getId()));
//
//        List<Slot> updatedList = filteredSlots.stream().
//                filter(e ->
//                    checkNow(e, bookedSlotsMap.get(e.getSalesManager().getId()))
//                )
//                .toList();
//
//        // Group slots by start date and count availability
//        Map<OffsetDateTime, Long> groupedSlots = updatedList.stream()
//                .collect(Collectors.groupingBy(Slot::getStartDate, Collectors.counting()));
//
//        List<AvailableSlotDto> collect = groupedSlots.entrySet().stream()
//                .map(entry -> new AvailableSlotDto(entry.getKey(), entry.getValue().intValue()))
//                .collect(Collectors.toList());
//
//        collect.sort(Comparator.comparing(AvailableSlotDto::getStartDate));
//        return collect;
//
//    }
//
//    private boolean checkNow(Slot availableSlot, List<Slot> slots) {
//        if (slots == null) return true;
//        return slots.stream().anyMatch(e -> !isDateOverlapped(e, availableSlot));
//    }
//
//    private boolean isDateOverlapped(Slot bookedSlot, Slot availableSlot) {
//        return bookedSlot.getStartDate().isBefore(availableSlot.getEndDate()) &&
//                bookedSlot.getEndDate().isAfter(availableSlot.getStartDate());
//    }
//
//    private boolean isSlotMatching(Slot slot, SlotRequestDto requestDto) {
//        SalesManager manager = slot.getSalesManager();
//        return manager.getLanguages().contains(requestDto.getLanguage())
//                && new HashSet<>(manager.getProducts()).containsAll(requestDto.getProducts())
//                && manager.getCustomerRatings().contains(requestDto.getRating());
//    }
//
//}