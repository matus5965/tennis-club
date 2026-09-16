package cz.inqool.tennisclub.mapper;

import cz.inqool.tennisclub.dto.courttype.CourtTypeResponse;
import cz.inqool.tennisclub.entity.CourtType;
import org.springframework.stereotype.Component;

@Component
public class CourtTypeMapper {
    public CourtTypeResponse toResponse(CourtType entity) {
        return new CourtTypeResponse(entity.getId(), entity.getName(), entity.getPricePerMinute());
    }
}
