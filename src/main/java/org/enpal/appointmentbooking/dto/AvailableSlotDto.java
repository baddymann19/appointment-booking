package org.enpal.appointmentbooking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableSlotDto {
    @JsonProperty("start_date")
    private OffsetDateTime startDate;
    @JsonProperty("available_count")
    private int availableCount;
}