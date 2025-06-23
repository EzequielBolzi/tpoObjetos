package com.tpo.armarPartido.repository;

import com.tpo.armarPartido.model.Comentario;
import jakarta.persistence.*;
import java.util.List;

public class ComentarioRepository {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public void save(Comentario comentario) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (comentario.getId() == null) {
                em.persist(comentario);
            } else {
                em.merge(comentario);
            }
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
            em.close();
        }
    }

    public Comentario findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Comentario.class, id);
        } finally {
            em.close();
        }
    }

    public List<Comentario> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Comentario c", Comentario.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void delete(Comentario comentario) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Comentario managed = em.find(Comentario.class, comentario.getId());
            if (managed != null) {
                em.remove(managed);
            }
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
            em.close();
        }
    }

    public void deleteByPartido(Long partidoId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            int deletedCount = em.createQuery("DELETE FROM Comentario c WHERE c.partido.id = :partidoId")
                .setParameter("partidoId", partidoId)
                .executeUpdate();
            tx.commit();
            System.out.println("Eliminados " + deletedCount + " comentarios del partido " + partidoId);
        } finally {
            if (tx.isActive()) tx.rollback();
            em.close();
        }
    }
} 