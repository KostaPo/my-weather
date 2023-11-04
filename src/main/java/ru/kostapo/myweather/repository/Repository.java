package ru.kostapo.myweather.repository;

import java.util.Optional;


public interface Repository<T,K> {
    T save(final T entity);
    Optional<T> findByKey(final K key);
    void deleteByKey(final K key);

}
