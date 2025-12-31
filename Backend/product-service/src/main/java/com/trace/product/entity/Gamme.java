package com.trace.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gamme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le code de la gamme est obligatoire")
    @Size(max = 50, message = "Le code ne peut pas dépasser 50 caractères")
    @Column(nullable = false, unique = true)
    private String code;

    @NotBlank(message = "Le nom de la gamme est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    @Column(nullable = false)
    private String nom;

    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    private String description;

    @OneToMany(mappedBy = "gamme")
    private List<Product> products;
}
