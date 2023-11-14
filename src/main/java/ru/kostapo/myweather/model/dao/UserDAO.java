package ru.kostapo.myweather.model.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import ru.kostapo.myweather.exception.UniqConstraintViolationException;
import ru.kostapo.myweather.exception.UserNotFoundException;
import ru.kostapo.myweather.model.User;

import javax.persistence.PersistenceException;
import java.util.Optional;

public class UserDAO {

    private final SessionFactory sessionFactory;

    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public User save(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            return user;
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new UniqConstraintViolationException("Нарушение ограничений уникальности");
            }
            return null;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Невозможно сохранить юзера", e);
        }
    }

    public Optional<User> findByLogin(String login) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session
                    .createQuery("SELECT u FROM User u LEFT JOIN FETCH u.sessions WHERE u.login = :login", User.class)
                    .setParameter("login", login)
                    .uniqueResult();
            transaction.commit();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new UserNotFoundException("Невозможно найти по логину");
        }
    }
}
