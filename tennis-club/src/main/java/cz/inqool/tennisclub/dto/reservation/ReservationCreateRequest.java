package cz.inqool.tennisclub.dto.reservation;

import cz.inqool.tennisclub.entity.GameType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservationCreateRequest(
        @NotBlank String courtNumber,
        @NotBlank String phoneNumber,
        @NotBlank String customerName,
        @NotNull @Future LocalDateTime startTime,
        @NotNull @Future LocalDateTime endTime,
        @NotNull GameType gameType
) {}
