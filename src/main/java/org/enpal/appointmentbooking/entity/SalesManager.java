package org.enpal.appointmentbooking.entity;

import lombok.Data;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import org.enpal.appointmentbooking.config.CustomStringListType;
import org.hibernate.annotations.Type;


import java.util.List;

@Entity
@Table(name = "sales_managers")
@Data
public class SalesManager {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    @Type(value = CustomStringListType.class)
    private List<String> languages;
    @Type(value = CustomStringListType.class)
    private List<String> products;
    @Type(value = CustomStringListType.class)
    private List<String> customerRatings;


}
