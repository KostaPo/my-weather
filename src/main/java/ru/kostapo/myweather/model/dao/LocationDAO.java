package ru.kostapo.myweather.model.dao;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import ru.kostapo.myweather.exception.UniqConstraintViolationException;
import ru.kostapo.myweather.model.Location;

import javax.persistence.PersistenceException;
import java.util.List;

public class LocationDAO {

    private final SessionFactory sessionFactory;

    public LocationDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Location save(Location location) {
        Transaction transaction = null;
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(location);
            transaction.commit();
            return location;
        } catch (PersistenceException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new UniqConstraintViolationException("Нарушение ограничений уникальности");
            }
            return null;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new HibernateException("Невозможно сохранить локацию", e);
        }
    }

    public void deleteById(Long id) {
        Transaction transaction = null;
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM Location WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new HibernateException("Can't delete location", e);
        }
    }

    public List<Location> findByUserId(Long userId) {
        Transaction transaction = null;
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            List<Location> locations = session.createQuery("SELECT l FROM Location l WHERE l.user.id = :userId", Location.class)
                    .setParameter("userId", userId)
                    .getResultList();
            transaction.commit();
            return locations;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new HibernateException("Can't retrieve locations", e);
        }
    }
}