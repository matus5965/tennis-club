package cz.inqool.tennisclub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "court")
@Getter @Setter
public class Court extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_type_id", nullable = false)
    private CourtType courtType;
}
