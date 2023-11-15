package ru.kostapo.myweather.model.dao;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.kostapo.myweather.model.Session;

import java.time.LocalDateTime;
import java.util.Optional;

public class SessionDAO {

    private final SessionFactory sessionFactory;

    public SessionDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session save(Session entity) {
        Transaction transaction = null;
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Невозможно сохранить сессию", e);
        }
    }

    public Optional<Session> findById(String uuid) {
        Optional<Session> userSession;
        Transaction transaction = null;
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String hql = "SELECT s FROM Session s LEFT JOIN FETCH s.user WHERE s.id = :uuid";
            Query<Session> query = session.createQuery(hql, Session.class);
            query.setParameter("uuid", uuid);
            userSession = Optional.ofNullable(query.uniqueResult());
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Не удалось найти по uuid", e);
        }
        return userSession;
    }

    public void deleteById(String uuid) {
        Transaction transaction = null;
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM Session WHERE id = :uuid")
                    .setParameter("uuid", uuid)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Can't delete session", e);
        }
    }

    public void deleteExpiredSessions(LocalDateTime currentTime) {
        Transaction transaction = null;
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String queryString = "DELETE FROM Session WHERE expiresAt <= :currentTime";
            session.createQuery(queryString)
                    .setParameter("currentTime", currentTime)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Can't delete exp session", e);
        }
    }
}
