package cz.inqool.tennisclub.dto.court;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourtSaveRequest(
        @NotBlank String courtNumber,
        @NotNull Long courtTypeId
) {}
