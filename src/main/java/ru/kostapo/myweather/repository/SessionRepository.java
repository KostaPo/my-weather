package ru.kostapo.myweather.repository;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.kostapo.myweather.model.Session;
import ru.kostapo.myweather.utils.HibernateUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class SessionRepository implements Repository<Session, String> {

    @Override
    public Session save(Session entity) {
        Transaction transaction = null;
        try (org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Can't save entity", e);
        }
    }

    @Override
    public Optional<Session> findByKey(String uuid) {
        Transaction transaction = null;
        try (org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Session userSession = session
                    .createQuery("SELECT s FROM Session s WHERE s.id = :uuid", Session.class)
                    .setParameter("uuid", uuid)
                    .uniqueResult();
            transaction.commit();
            return Optional.ofNullable(userSession);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Can't find by uuid", e);
        }
    }

    @Override
    public void deleteByKey(String uuid) {
        Transaction transaction = null;
        try (org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession()) {
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

    public List<Session> getExpiredSessions(LocalDateTime current) {
        Transaction transaction = null;
        try (org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String queryString = "SELECT s FROM Session s WHERE s.expiresAt < :current";
            Query query = session.createQuery(queryString);
            query.setParameter("current", current);
            transaction.commit();
            return (List<Session>) query.getResultList();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Can't find exp session", e);
        }
    }
}
