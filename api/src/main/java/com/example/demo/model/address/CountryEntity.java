
package com.example.demo.model.address;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity(name = "country")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CountryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(nullable = false)
    @NonNull
    private String name;
}