package com.tpo.armarPartido.repository;

import com.tpo.armarPartido.model.Partido;
import jakarta.persistence.*;
import java.util.List;

public class PartidoRepository {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public void save(Partido partido) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (partido.getId() == null) {
                em.persist(partido);
            } else {
                em.merge(partido);
            }
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
            em.close();
        }
    }

    public Partido findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Partido.class, id);
        } finally {
            em.close();
        }
    }

    public List<Partido> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Partido p", Partido.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void delete(Partido partido) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Partido managed = em.find(Partido.class, partido.getId());
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