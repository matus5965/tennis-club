package cz.inqool.tennisclub.dto.reservation;

import cz.inqool.tennisclub.entity.GameType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        String courtNumber,
        String customerName,
        String phoneNumber,
        LocalDateTime startTime,
        LocalDateTime endTime,
        GameType gameType,
        BigDecimal price,
        LocalDateTime createdAt
) {}