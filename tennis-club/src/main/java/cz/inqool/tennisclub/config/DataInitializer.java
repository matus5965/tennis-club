package cz.inqool.tennisclub.config;

import cz.inqool.tennisclub.dao.CourtDao;
import cz.inqool.tennisclub.dao.CourtTypeDao;
import cz.inqool.tennisclub.entity.Court;
import cz.inqool.tennisclub.entity.CourtType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AppProperties appProperties;
    private final CourtTypeDao courtTypeDao;
    private final CourtDao courtDao;

    public DataInitializer(AppProperties appProperties, CourtTypeDao courtTypeDao, CourtDao courtDao) {
        this.appProperties = appProperties;
        this.courtTypeDao = courtTypeDao;
        this.courtDao = courtDao;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!appProperties.dataInitEnabled()) {
            return;
        }

        CourtType clay = new CourtType();
        clay.setName("Clay");
        clay.setPricePerMinute(new BigDecimal("0.50"));
        clay = courtTypeDao.save(clay);

        CourtType hard = new CourtType();
        hard.setName("Hard court");
        hard.setPricePerMinute(new BigDecimal("0.70"));
        hard = courtTypeDao.save(hard);

        createCourt("1", clay);
        createCourt("2", clay);
        createCourt("3", hard);
        createCourt("4", hard);
    }

    private void createCourt(String courtNumber, CourtType type) {
        Court court = new Court();
        court.setNumber(courtNumber);
        court.setCourtType(type);
        courtDao.save(court);
    }
}
