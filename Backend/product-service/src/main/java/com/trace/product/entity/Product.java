package com.trace.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.trace.product.enums.TypeGestion;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le code du produit est obligatoire")
    @Size(max = 50, message = "Le code ne peut pas dépasser 50 caractères")
    @Column(nullable = false, unique = true)
    private String code;

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    @Column(nullable = false)
    private String nom;

    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être positif")
    private Double prix;

    @Min(value = 0, message = "Le rang doit être positif ou nul")
    private Integer rang;

    private TypePalette idTypePalPro;

    private TypeGestion typeGestion;

    @Min(value = 0, message = "L'unité de base doit être positive ou nulle")
    private Integer unibase;

    @Min(value = 0, message = "L'unité par palette doit être positive ou nulle")
    private Integer upal;

    @Min(value = 0, message = "L'unité par caisse doit être positive ou nulle")
    private Integer ucas;

    @Builder.Default
    private Boolean actif = true;

    @DecimalMin(value = "0.0", inclusive = false, message = "La quantité minimale doit être positive")
    private Double minqu;

    @DecimalMin(value = "0.0", inclusive = false, message = "La quantité maximale doit être positive")
    private Double maxqu;

    @Min(value = 0, message = "La péremption doit être positive ou nulle")
    private Integer peremption;

    @DecimalMin(value = "0.0", inclusive = false, message = "Le grammage doit être positif")
    private Double grammage;

    @ManyToOne
    @JoinColumn(name = "gamme_id")
    private Gamme gamme;
}
