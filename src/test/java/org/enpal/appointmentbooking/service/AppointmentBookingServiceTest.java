package org.enpal.appointmentbooking.service;

import org.enpal.appointmentbooking.dto.AvailableSlotDto;
import org.enpal.appointmentbooking.dto.SlotRequestDto;
import org.enpal.appointmentbooking.entity.SalesManager;
import org.enpal.appointmentbooking.entity.Slot;
import org.enpal.appointmentbooking.repo.SlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
@ExtendWith(MockitoExtension.class)
class AppointmentBookingServiceTest {

    @Mock
    private SlotRepository slotRepository;

    @InjectMocks
    private AppointmentBookingService service;

    @Test
    void testNoAvailableSlots() {
        // Arrange
        LocalDate queryDate = LocalDate.of(2024, 5, 3);
        OffsetDateTime startOfDay = queryDate.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        OffsetDateTime endOfDay = startOfDay.plusDays(1);
        SlotRequestDto requestDto = new SlotRequestDto(queryDate, List.of("ProductA"), "English", "Gold");
        when(slotRepository.findByBookedFalseAndStartDateBetween(startOfDay, endOfDay)).thenReturn(Collections.emptyList());
        when(slotRepository.findByBookedTrueAndStartDateBetween(startOfDay, endOfDay)).thenReturn(Collections.emptyList());

        // Act
        List<AvailableSlotDto> result = service.findAvailableSlots(requestDto);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void testNoBookedSlots() {
        // Arrange
        LocalDate queryDate = LocalDate.of(2024, 5, 3);
        OffsetDateTime startOfDay = queryDate.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        OffsetDateTime endOfDay = startOfDay.plusDays(1);

        SalesManager salesManager = new SalesManager();
        salesManager.setId(1);
        salesManager.setLanguages(List.of("English"));
        salesManager.setProducts(List.of("ProductA"));
        salesManager.setCustomerRatings(List.of("Gold"));

        Slot availableSlot1 = new Slot(1, startOfDay.plusHours(10), startOfDay.plusHours(11), false, salesManager);
        Slot availableSlot2 = new Slot(2, startOfDay.plusHours(11), startOfDay.plusHours(12), false, salesManager);

        SlotRequestDto requestDto = new SlotRequestDto(queryDate, List.of("ProductA"),"English", "Gold");

        when(slotRepository.findByBookedFalseAndStartDateBetween(startOfDay, endOfDay)).thenReturn(Arrays.asList(availableSlot1, availableSlot2));
        when(slotRepository.findByBookedTrueAndStartDateBetween(startOfDay, endOfDay)).thenReturn(Collections.emptyList());

        // Act
        List<AvailableSlotDto> result = service.findAvailableSlots(requestDto);

        // Assert
        assertEquals(2, result.size());
        assertEquals(startOfDay.plusHours(10), result.get(0).getStartDate());
        assertEquals(1, result.get(0).getAvailableCount());
    }

    @Test
    void testMultipleOverlappingBookedSlots() {
        // Arrange
        LocalDate queryDate = LocalDate.of(2024, 5, 3);
        OffsetDateTime startOfDay = queryDate.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        OffsetDateTime endOfDay = startOfDay.plusDays(1);

        SalesManager salesManager = new SalesManager();
        salesManager.setId(1);
        salesManager.setLanguages(List.of("English"));
        salesManager.setProducts(List.of("ProductA"));
        salesManager.setCustomerRatings(List.of("Gold"));

        Slot availableSlot1 = new Slot(1, startOfDay.plusHours(10), startOfDay.plusHours(11), false, salesManager);
        Slot availableSlot2 = new Slot(2, startOfDay.plusHours(11), startOfDay.plusHours(12), false, salesManager);

        Slot bookedSlot1 = new Slot(3, startOfDay.plusHours(10).plusMinutes(30), startOfDay.plusHours(11).plusMinutes(30), true, salesManager);
        Slot bookedSlot2 = new Slot(4, startOfDay.plusHours(11).plusMinutes(15), startOfDay.plusHours(11).plusMinutes(45), true, salesManager);

        SlotRequestDto requestDto = new SlotRequestDto(queryDate, List.of("ProductA"),"English", "Gold");

        when(slotRepository.findByBookedFalseAndStartDateBetween(startOfDay, endOfDay)).thenReturn(Arrays.asList(availableSlot1, availableSlot2));
        when(slotRepository.findByBookedTrueAndStartDateBetween(startOfDay, endOfDay)).thenReturn(Arrays.asList(bookedSlot1, bookedSlot2));

        // Act
        List<AvailableSlotDto> result = service.findAvailableSlots(requestDto);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void testDifferentSalesManagers() {
        // Arrange
        LocalDate queryDate = LocalDate.of(2024, 5, 3);
        OffsetDateTime startOfDay = queryDate.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        OffsetDateTime endOfDay = startOfDay.plusDays(1);

        SalesManager manager1 = new SalesManager();
        manager1.setId(1);
        manager1.setLanguages(List.of("English"));
        manager1.setProducts(List.of("ProductA"));
        manager1.setCustomerRatings(List.of("Gold"));

        SalesManager manager2 = new SalesManager();
        manager2.setId(2);
        manager2.setLanguages(List.of("German"));
        manager2.setProducts(List.of("ProductB"));
        manager2.setCustomerRatings(List.of("Silver"));

        Slot slot1 = new Slot(1, startOfDay.plusHours(10), startOfDay.plusHours(11), false, manager1);
        Slot slot2 = new Slot(2, startOfDay.plusHours(11), startOfDay.plusHours(12), false, manager2);

        SlotRequestDto requestDto = new SlotRequestDto(queryDate, List.of("ProductA"),"English", "Gold");

        when(slotRepository.findByBookedFalseAndStartDateBetween(startOfDay, endOfDay)).thenReturn(Arrays.asList(slot1, slot2));
        when(slotRepository.findByBookedTrueAndStartDateBetween(startOfDay, endOfDay)).thenReturn(Collections.emptyList());

        // Act
        List<AvailableSlotDto> result = service.findAvailableSlots(requestDto);

        // Assert
        assertEquals(1, result.size());
        assertEquals(startOfDay.plusHours(10), result.get(0).getStartDate());
    }

    @Test
    void testFindAvailableSlots() {
        // Arrange
        LocalDate queryDate = LocalDate.of(2024, 5, 3);
        OffsetDateTime startOfDay = queryDate.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        OffsetDateTime endOfDay = startOfDay.plusDays(1);

        SalesManager salesManager = new SalesManager();
        salesManager.setId(1);
        salesManager.setLanguages(List.of("English"));
        salesManager.setProducts(List.of("ProductA"));
        salesManager.setCustomerRatings(List.of("Gold"));

        Slot availableSlot1 = new Slot(1, startOfDay.plusHours(9), startOfDay.plusHours(10), false, salesManager);
        Slot availableSlot2 = new Slot(2, startOfDay.plusHours(11), startOfDay.plusHours(12), false, salesManager);

        Slot bookedSlot = new Slot(3, startOfDay.plusHours(10).plusMinutes(30), startOfDay.plusHours(11).plusMinutes(30), true, salesManager);

        SlotRequestDto requestDto = new SlotRequestDto(queryDate, List.of("ProductA"),"English", "Gold");

        when(slotRepository.findByBookedFalseAndStartDateBetween(startOfDay, endOfDay)).thenReturn(Arrays.asList(availableSlot1, availableSlot2));
        when(slotRepository.findByBookedTrueAndStartDateBetween(startOfDay, endOfDay)).thenReturn(Collections.singletonList(bookedSlot));

        // Act
        List<AvailableSlotDto> result = service.findAvailableSlots(requestDto);

        // Assert
        assertEquals(1, result.size());
        assertEquals(startOfDay.plusHours(9), result.get(0).getStartDate());
        assertEquals(1, result.get(0).getAvailableCount());

        verify(slotRepository).findByBookedFalseAndStartDateBetween(startOfDay, endOfDay);
        verify(slotRepository).findByBookedTrueAndStartDateBetween(startOfDay, endOfDay);
        verifyNoMoreInteractions(slotRepository);
    }

}
