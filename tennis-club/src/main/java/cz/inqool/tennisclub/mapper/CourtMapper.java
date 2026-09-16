package cz.inqool.tennisclub.mapper;

import cz.inqool.tennisclub.dto.court.CourtResponse;
import cz.inqool.tennisclub.entity.Court;
import org.springframework.stereotype.Component;

@Component
public class CourtMapper {
    public CourtResponse toResponse(Court entity) {
        return new CourtResponse(
                entity.getId(),
                entity.getNumber(),
                entity.getCourtType().getName(),
                entity.getCourtType().getPricePerMinute()
        );
    }
}
