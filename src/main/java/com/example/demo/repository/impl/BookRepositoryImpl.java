package com.example.demo.repository.impl;

import com.example.demo.exception.DataProcessingException;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    private final EntityManagerFactory entityManagerFactory;

    @Override
    public Book createBook(Book book) {
        EntityTransaction transaction = null;
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(book);
            transaction.commit();
            return book;
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't save book: " + book, e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Book> getAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("from Book", Book.class).getResultList();
        } catch (RuntimeException e) {
            throw new DataProcessingException("All books were not found", e);
        }
    }

    @Override
    public Book getBookById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("from Book where id = :id", Book.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (RuntimeException e) {
            throw new DataProcessingException("All books were not found", e);
        }
    }
}
