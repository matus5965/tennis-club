package cz.inqool.tennisclub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customer")
@Getter @Setter
public class Customer extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String name;
}
