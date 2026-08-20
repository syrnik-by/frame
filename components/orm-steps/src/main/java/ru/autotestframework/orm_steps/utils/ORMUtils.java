package ru.autotestframework.orm_steps.utils;

import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.cucumber.type.Triple;

/**
 * Orm utils.
 */
@UtilityClass
public class ORMUtils {

    /**
     * Gets all objects.
     *
     * @param sessionFactory the session factory
     * @param clazz          the clazz
     * @return the all objects
     */
    public static List<Object> getAllObjects(SessionFactory sessionFactory, Class<?> clazz) {
        try (var session = sessionFactory.openSession()) {
            Query<?> query = session.createQuery("from " + clazz.getName(), clazz);
            return (List<Object>) query.list();
        }
    }

    /**
     * Gets by id.
     *
     * @param sessionFactory the session factory
     * @param clazz          the clazz
     * @param id             the id
     * @return the by id
     */
    public static Object getById(SessionFactory sessionFactory, Class<?> clazz, Long id) {
        try (var session = sessionFactory.openSession()) {
            return session.get(clazz, id);
        }
    }

    /**
     * Gets by field.
     *
     * @param sessionFactory the session factory
     * @param clazz          the clazz
     * @param list           the list
     * @return the by field
     */
    public static List<Object> getByField(SessionFactory sessionFactory, Class<?> clazz, List<Triple> list) {
        try (var session = sessionFactory.openSession()) {
            StringBuilder sb = new StringBuilder();
            sb.append("FROM ").append(clazz.getName());

            if (list != null && !list.isEmpty()) {
                sb.append(" WHERE ");
                List<String> conditions = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {
                    conditions.add(list.get(i).getFirst() + " " + list.get(i).getSecond() + " :param" + i);
                }

                sb.append(String.join(" AND ", conditions));
            }

            Query<?> query = session.createQuery(sb.toString(), clazz);

            for (int i = 0; i < list.size(); i++) {
                query.setParameter("param" + i, list.get(i).getThird());
            }

            return (List<Object>) query.list();
        }
    }

    /**
     * Insert object.
     *
     * @param sessionFactory the session factory
     * @param object         the object
     */
    public static void insertObject(SessionFactory sessionFactory, Object object) {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(object);
            session.getTransaction().commit();
        }
    }

    /**
     * Update object.
     *
     * @param sessionFactory the session factory
     * @param object         the object
     */
    public static void updateObject(SessionFactory sessionFactory, Object object) {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(object);
            session.getTransaction().commit();
        }
    }

    /**
     * Delete object.
     *
     * @param sessionFactory the session factory
     * @param object         the object
     * @param list           the list
     */
    public static void deleteObject(SessionFactory sessionFactory, Object object, List<Triple> list) {
        if (list.isEmpty()) {
            try (var session = sessionFactory.openSession()) {
                session.beginTransaction();
                session.delete(object);
                session.getTransaction().commit();
            }
        } else {
            try {
                object = getByField(sessionFactory, (Class<?>) object, list).get(0);
            } catch (IndexOutOfBoundsException e) {
                throw new AutotestException("Объект отсутствует в базе данных!");
            }
            deleteObject(sessionFactory, object, List.of());
        }
    }
}
