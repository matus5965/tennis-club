package cz.inqool.tennisclub.dao;

import cz.inqool.tennisclub.entity.BaseEntity;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T extends BaseEntity, ID> implements GenericDao<T, ID> {

    protected final EntityManager entityManager;
    private final Class<T> entityClass;

    protected AbstractDao(EntityManager entityManager, Class<T> entityClass) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
    }

    @Override
    public T save(T entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
            return entity;
        }
        return entityManager.merge(entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        T entity = entityManager.find(entityClass, id);
        return Optional.ofNullable(entity)
                .filter(e -> !e.isDeleted());
    }

    @Override
    public List<T> findAll() {
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.deleted = false";
        return entityManager.createQuery(jpql, entityClass).getResultList();
    }

    @Override
    public void delete(T entity) {
        entity.setDeleted(true);
        entityManager.merge(entity);
    }
}
