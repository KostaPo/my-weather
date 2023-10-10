package ru.kostapo.myweather.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name="sessions",
        indexes = {@Index(name = "expires_idx", columnList = "expiresAt")})
public class Session {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    @NotNull
    LocalDateTime expiresAt;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
