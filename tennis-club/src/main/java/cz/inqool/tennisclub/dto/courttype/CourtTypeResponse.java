package cz.inqool.tennisclub.dto.courttype;

import java.math.BigDecimal;

public record CourtTypeResponse(
        Long id,
        String name,
        BigDecimal pricePerMinute
) {}
