package org.enpal.appointmentbooking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotRequestDto implements Serializable {
    @NotNull
    private LocalDate date;
    @NotNull
    private List<String> products;
    @NotBlank(message = "language cannot be empty")
    private String language;
    @NotBlank(message = "rating cannot be empty")
    private String rating;
}