package cz.inqool.tennisclub.dao;

import cz.inqool.tennisclub.dao.impl.CourtDaoImpl;
import cz.inqool.tennisclub.entity.Court;
import cz.inqool.tennisclub.entity.CourtType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(CourtDaoImpl.class)
public class CourtDaoImplTest {

    @Autowired
    private CourtDaoImpl courtDao;

    @Autowired
    private TestEntityManager entityManager;

    private CourtType courtType;

    @BeforeEach
    public void setUp() {
        courtType = new CourtType();
        courtType.setName("Clay");
        courtType.setPricePerMinute(BigDecimal.valueOf(10.0));
        entityManager.persistAndFlush(courtType);
    }

    @Test
    public void findByCourtNumber_WhenExistsAndNotDeleted_ReturnsCourt() {
        Court court = new Court();
        court.setNumber("Court 1");
        court.setCourtType(courtType);
        court.setDeleted(false);
        entityManager.persistAndFlush(court);

        Optional<Court> result = courtDao.findByCourtNumber("Court 1");

        assertThat(result).isPresent();
        assertThat(result.get().getNumber()).isEqualTo("Court 1");
    }

    @Test
    public void findByCourtNumber_WhenSoftDeleted_ReturnsEmpty() {
        Court court = new Court();
        court.setNumber("Court 2");
        court.setCourtType(courtType);
        court.setDeleted(true);
        entityManager.persistAndFlush(court);

        Optional<Court> result = courtDao.findByCourtNumber("Court 2");

        assertThat(result).isEmpty();
    }

    @Test
    public void findByCourtNumber_WhenNotFound_ReturnsEmpty() {
        Optional<Court> result = courtDao.findByCourtNumber("non-existing number");

        assertThat(result).isEmpty();
    }
}