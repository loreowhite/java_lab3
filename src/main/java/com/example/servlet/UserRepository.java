package com.example.servlet;

import jakarta.persistence.*;

public class UserRepository {

    public static void save(User user) {
        EntityManager em = JpaUtil.emf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(user);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public static User findByLogin(String login) {
        EntityManager em = JpaUtil.emf().createEntityManager();
        try {
            TypedQuery<User> q = em.createQuery(
                    "select u from User u where u.login = :login", User.class);
            q.setParameter("login", login);
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public static boolean checkCredentials(String login, String password) {
        EntityManager em = JpaUtil.emf().createEntityManager();
        try {
            TypedQuery<Long> q = em.createQuery(
                    "select count(u) from User u where u.login = :login and u.password = :password",
                    Long.class);
            q.setParameter("login", login);
            q.setParameter("password", password);
            Long cnt = q.getSingleResult();
            return cnt != null && cnt > 0;
        } finally {
            em.close();
        }
    }
}