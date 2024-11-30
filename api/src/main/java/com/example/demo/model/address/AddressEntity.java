package com.example.demo.model.address;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "address")
@Data
@NoArgsConstructor
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(nullable = false)
    private String description;
    @JoinColumn(name = "townId", nullable = false)
    @ManyToOne
    private TownEntity town;
    @JoinColumn(name = "countryId", nullable = false)
    @ManyToOne
    private CountryEntity country;
    @JoinColumn(name = "stateId", nullable = false)
    @ManyToOne
    private StateEntity state;
    @JoinColumn(name = "postCodeId", nullable = false)
    @ManyToOne
    private PostCodeEntity postCode;

    @Column(nullable = false)
    private String street;
}