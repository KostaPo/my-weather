package ru.kostapo.myweather.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import ru.kostapo.myweather.exception.UniqConstraintViolationException;
import ru.kostapo.myweather.utils.HibernateUtil;

import javax.persistence.PersistenceException;
import java.util.Optional;


public interface Repository<T,K> {
    T save(final T entity);
    Optional<T> findByKey(final K key);
}
