package ru.kostapo.myweather.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name="locations")
public class Location {

    private static final String SEQ_NAME = "location_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;

    @NotEmpty(message = "Введите название локации!")
    private String name;

    @NotNull(message = "Укажите широту!")
    private BigDecimal latitude;

    @NotNull(message = "Укажите долготу!")
    private BigDecimal longitude;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
