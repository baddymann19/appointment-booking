package org.enpal.appointmentbooking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotRequestDto implements Serializable {

    @NotNull(message = "date cannot be empty")
    private LocalDate date;

    @NotNull(message = "Products list cannot be null")
    @Size(min = 1, message = "At least one product must be specified")
    private List<@NotBlank(message = "Product names cannot be empty") String> products;

    @NotBlank(message = "language cannot be empty")
    private String language;
    @NotBlank(message = "rating cannot be empty")
    private String rating;
}