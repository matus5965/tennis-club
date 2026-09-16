package cz.inqool.tennisclub.dao;

import cz.inqool.tennisclub.entity.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractDaoTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<TestEntity> typedQuery;

    private TestDao testDao;

    static class TestEntity extends BaseEntity {
        private Long id;
        private boolean deleted;

        @Override public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        @Override public boolean isDeleted() { return deleted; }
        @Override public void setDeleted(boolean deleted) { this.deleted = deleted; }
    }

    static class TestDao extends AbstractDao<TestEntity, Long> {
        public TestDao(EntityManager entityManager) {
            super(entityManager, TestEntity.class);
        }
    }

    @BeforeEach
    void setUp() {
        testDao = new TestDao(entityManager);
    }

    @Test
    public void save() {
        TestEntity entity = new TestEntity();

        TestEntity result = testDao.save(entity);

        verify(entityManager).persist(entity);
        assertThat(result).isEqualTo(entity);
    }

    @Test
    public void findById() {
        TestEntity entity = new TestEntity();
        entity.setId(1L);
        entity.setDeleted(false);
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(entity);

        Optional<TestEntity> result = testDao.findById(1L);

        assertThat(result).contains(entity);
    }

    @Test
    public void findAll() {
        TestEntity entity = new TestEntity();
        when(entityManager.createQuery("SELECT e FROM TestEntity e WHERE e.deleted = false", TestEntity.class))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(entity));

        List<TestEntity> result = testDao.findAll();

        assertThat(result).containsExactly(entity);
    }

    @Test
    public void delete() {
        TestEntity entity = new TestEntity();
        entity.setDeleted(false);

        testDao.delete(entity);

        assertThat(entity.isDeleted()).isTrue();
        verify(entityManager).merge(entity);
    }
}