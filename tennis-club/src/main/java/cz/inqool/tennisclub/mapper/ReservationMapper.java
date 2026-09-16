package cz.inqool.tennisclub.mapper;

import cz.inqool.tennisclub.dto.reservation.ReservationResponse;
import cz.inqool.tennisclub.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    public ReservationResponse toResponse(Reservation entity) {
        return new ReservationResponse(
                entity.getId(),
                entity.getCourt().getNumber(),
                entity.getCustomer().getName(),
                entity.getCustomer().getPhoneNumber(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getGameType(),
                entity.getPrice(),
                entity.getCreatedAt()
        );
    }
}
