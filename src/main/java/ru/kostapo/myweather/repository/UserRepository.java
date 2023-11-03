package ru.kostapo.myweather.repository;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import ru.kostapo.myweather.exception.UniqConstraintViolationException;
import ru.kostapo.myweather.model.User;
import ru.kostapo.myweather.utils.HibernateUtil;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

    public class UserRepository implements Repository<User, String> {

    @Override
    public User save(User entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
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
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);

            Predicate predicate = builder.equal(root.get("login"), login);
            query.select(root).where(predicate);

            User user = session.createQuery(query).uniqueResult();
            transaction.commit();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Can't save entity", e);
        }
    }
}
