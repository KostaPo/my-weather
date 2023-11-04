package ru.kostapo.myweather.repository;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import ru.kostapo.myweather.exception.UniqConstraintViolationException;
import ru.kostapo.myweather.model.User;
import ru.kostapo.myweather.utils.HibernateUtil;

import javax.persistence.PersistenceException;
import java.util.Optional;

    public class UserRepository implements Repository<User, String> {

    @Override
    public User save(User entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
            return entity;
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new UniqConstraintViolationException("НАРУШЕНИЕ ОГРАНИЧЕНИЙ УНИКАЛЬНОСТИ");
            }
            return null;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Can't save entity", e);
        }
    }

    @Override
    public Optional<User> findByKey(String login) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
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
            throw new HibernateException("Can't save entity", e);
        }
    }

        @Override
        public void deleteByKey(String login) {
            /*Transaction transaction = null;
            try (org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                Query<?> query = session.createQuery("DELETE FROM User WHERE login = :login");
                query.setParameter("login", login);
                query.executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new HibernateException("Can't delete user", e);
            }*/
        }
    }
