package ru.kostapo.myweather.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name="users",
        uniqueConstraints = @UniqueConstraint(columnNames = "login"),
        indexes = {@Index(name = "login_idx", columnList = "login")})
public class User {

    private static final String SEQ_NAME = "user_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;

    @NotEmpty(message = "Введите логин!")
    @Size(min = 3, max = 32, message = "Логин должен быть от 3 до 32 символов!")
    @Column(updatable=false)
    private String login;

    @NotEmpty(message = "Введите пароль!")
    @Size(min = 3, message = "Пароль должен быть от 3 символов!")
    private String password;

    //TODO переделать на treeset сорт по add time
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Location> locations = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Session session;
}
