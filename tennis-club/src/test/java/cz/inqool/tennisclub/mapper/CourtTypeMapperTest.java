package cz.inqool.tennisclub.mapper;

import cz.inqool.tennisclub.dto.courttype.CourtTypeResponse;
import cz.inqool.tennisclub.entity.CourtType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CourtTypeMapperTest {

    private CourtTypeMapper courtTypeMapper;

    @BeforeEach
    void setUp() {
        courtTypeMapper = new CourtTypeMapper();
    }

    @Test
    void toResponse_ShouldMapAllFieldsCorrectly() {
        CourtType entity = new CourtType();
        entity.setId(10L);
        entity.setName("Clay");
        entity.setPricePerMinute(new BigDecimal("0.25"));

        CourtTypeResponse response = courtTypeMapper.toResponse(entity);

        assertNotNull(response);
        assertEquals(entity.getId(), response.id());
        assertEquals(entity.getName(), response.name());
        assertEquals(entity.getPricePerMinute(), response.pricePerMinute());
    }
}