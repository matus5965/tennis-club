package cz.inqool.tennisclub.dto.court;

import java.math.BigDecimal;

public record CourtResponse(
        Long id,
        String courtNumber,
        String courtTypeName,
        BigDecimal pricePerMinute
) {}
