package cz.inqool.tennisclub.mapper;

import cz.inqool.tennisclub.dto.court.CourtResponse;
import cz.inqool.tennisclub.entity.Court;
import cz.inqool.tennisclub.entity.CourtType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CourtMapperTest {

    private CourtMapper courtMapper;

    @BeforeEach
    void setUp() {
        courtMapper = new CourtMapper();
    }

    @Test
    void toResponse_ShouldMapAllFieldsCorrectly() {
        CourtType courtType = new CourtType();
        courtType.setName("Grass");
        courtType.setPricePerMinute(new BigDecimal("0.50"));

        Court entity = new Court();
        entity.setId(5L);
        entity.setNumber("Court 2");
        entity.setCourtType(courtType);

        CourtResponse response = courtMapper.toResponse(entity);

        assertNotNull(response);
        assertEquals(entity.getId(), response.id());
        assertEquals(entity.getNumber(), response.courtNumber());
        assertEquals(courtType.getName(), response.courtTypeName());
        assertEquals(courtType.getPricePerMinute(), response.pricePerMinute());
    }
}