package org.enpal.appointmentbooking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "slots")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private boolean booked;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sales_manager_id")
    private SalesManager salesManager;
}
