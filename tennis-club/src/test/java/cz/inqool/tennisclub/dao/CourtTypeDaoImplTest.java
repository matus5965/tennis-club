package cz.inqool.tennisclub.dao;

import cz.inqool.tennisclub.dao.impl.CourtTypeDaoImpl;
import cz.inqool.tennisclub.entity.CourtType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(CourtTypeDaoImpl.class)
class CourtTypeDaoImplTest {

    @Autowired
    private CourtTypeDaoImpl courtTypeDao;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findByName_WhenExistsAndNotDeleted_ReturnsCourtType() {
        CourtType courtType = new CourtType();
        courtType.setName("Grass");
        courtType.setPricePerMinute(BigDecimal.valueOf(12.5));
        courtType.setDeleted(false);
        entityManager.persistAndFlush(courtType);

        Optional<CourtType> result = courtTypeDao.findByName("Grass");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Grass");
        assertThat(result.get().getPricePerMinute()).isEqualByComparingTo(BigDecimal.valueOf(12.5));
    }

    @Test
    public void findByName_WhenSoftDeleted_ReturnsEmpty() {
        CourtType courtType = new CourtType();
        courtType.setName("Hard");
        courtType.setPricePerMinute(BigDecimal.valueOf(8.0));
        courtType.setDeleted(true);
        entityManager.persistAndFlush(courtType);

        Optional<CourtType> result = courtTypeDao.findByName("Hard");

        assertThat(result).isEmpty();
    }

    @Test
    public void findByName_WhenNotFound_ReturnsEmpty() {
        Optional<CourtType> result = courtTypeDao.findByName("Clay");

        assertThat(result).isEmpty();
    }
}