package org.enpal.appointmentbooking.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.enpal.appointmentbooking.dto.AvailableSlotDto;
import org.enpal.appointmentbooking.dto.SlotRequestDto;
import org.enpal.appointmentbooking.service.AppointmentBookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AppointmentBookingService appointmentBookingService;

    @Test
    @SneakyThrows
    public void testGetAvailableSlots() {
        LocalDate queryDate = LocalDate.of(2024, 5, 3);
        SlotRequestDto slotRequestDto = new SlotRequestDto(queryDate, List.of("ProductA"), "English", "Gold");
         List<AvailableSlotDto> availableSlots = new ArrayList<>(); // fill in details
         given(appointmentBookingService.findAvailableSlots(slotRequestDto)).willReturn(availableSlots);
         mockMvc.perform(post("/calendar/query")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(slotRequestDto)))
                 .andExpect(status().isOk()) .andExpect(content().json(new ObjectMapper().writeValueAsString(availableSlots)));
    }

    @Test
    @SneakyThrows
    public void testGetAvailableSlots_with_incomplete_request() {
        LocalDate queryDate = LocalDate.of(2024, 5, 3);
        SlotRequestDto slotRequestDto = new SlotRequestDto(queryDate, List.of("ProductA"), "English", null);
         List<AvailableSlotDto> availableSlots = new ArrayList<>(); // fill in details
         given(appointmentBookingService.findAvailableSlots(slotRequestDto)).willReturn(availableSlots);
         mockMvc.perform(post("/calendar/query")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(slotRequestDto)))
                 .andExpect(status().isBadRequest())
                 .andExpect(jsonPath("$.detail").value("Validation failed for the provided request data."))
                 .andExpect(jsonPath("$.fieldErrors.rating").value("rating cannot be empty"));
    }
}