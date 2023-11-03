package ru.kostapo.myweather.repository;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import ru.kostapo.myweather.model.Session;
import ru.kostapo.myweather.utils.HibernateUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class SessionRepository implements Repository<Session, String> {

    @Override
    public Session save(Session entity) {
        Transaction transaction = null;
        try (org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
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
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Session> query = builder.createQuery(Session.class);
            Root<Session> root = query.from(Session.class);

            Predicate predicate = builder.equal(root.get("id"), uuid);
            query.select(root).where(predicate);

            Session userSession = session.createQuery(query).uniqueResult();
            transaction.commit();
            return Optional.ofNullable(userSession);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Can't save entity", e);
        }
    }
}
