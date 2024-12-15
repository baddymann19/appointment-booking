package org.enpal.appointmentbooking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.enpal.appointmentbooking.dto.SlotRequestDto;
import org.enpal.appointmentbooking.service.AppointmentBookingService;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
@Slf4j
public class AppointmentController {

    private final AppointmentBookingService appointmentBookingService;

    @ResponseBody
    @PostMapping("/query")
    public ResponseEntity<?> getAvailableSlots(@RequestBody @Valid @NotNull SlotRequestDto slotRequestDto) {
        return ResponseEntity.ok(appointmentBookingService.findAvailableSlots(slotRequestDto));
    }
}