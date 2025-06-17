package com.tpo.armarPartido.repository;

import com.tpo.armarPartido.model.Notificacion;
import jakarta.persistence.*;
import java.util.List;

public class NotificacionRepository {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public void save(Notificacion notificacion) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (notificacion.getId() == null) {
                em.persist(notificacion);
            } else {
                em.merge(notificacion);
            }
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
            em.close();
        }
    }

    public Notificacion findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Notificacion.class, id);
        } finally {
            em.close();
        }
    }

    public List<Notificacion> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT n FROM Notificacion n", Notificacion.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void delete(Notificacion notificacion) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Notificacion managed = em.find(Notificacion.class, notificacion.getId());
            if (managed != null) {
                em.remove(managed);
            }
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
            em.close();
        }
    }
} 