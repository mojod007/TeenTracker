package com.trace.entity;

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
import com.trace.entity.enums.TypeGestion;
import com.trace.entity.enums.TypePalette;

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
    @Size(max = 50, message = "Le code ne peut pas depasser 50 caracteres")
    @Column(nullable = false, unique = true)
    private String code;

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas depasser 100 caracteres")
    @Column(nullable = false)
    private String nom;

    @Size(max = 500, message = "La description ne peut pas depasser 500 caracteres")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit etre positif")
    private Double prix;

    @Min(value = 0, message = "Le rang doit etre positif ou nul")
    private Integer rang;

    private TypePalette idTypePalPro;

    private TypeGestion typeGestion;

    @Min(value = 0, message = "L'unite de base doit etre positive ou nulle")
    private Integer unibase;

    @Min(value = 0, message = "L'unite par palette doit etre positive ou nulle")
    private Integer upal;

    @Min(value = 0, message = "L'unite par caisse doit etre positive ou nulle")
    private Integer ucas;

    @Builder.Default
    private Boolean actif = true;

    @DecimalMin(value = "0.0", inclusive = false, message = "La quantite minimale doit etre positive")
    private Double minqu;

    @DecimalMin(value = "0.0", inclusive = false, message = "La quantite maximale doit etre positive")
    private Double maxqu;

    @Min(value = 0, message = "La peremption doit etre positive ou nulle")
    private Integer peremption;

    @DecimalMin(value = "0.0", inclusive = false, message = "Le grammage doit etre positif")
    private Double grammage;

    @ManyToOne
    @JoinColumn(name = "gamme_id")
    private Gamme gamme;
}
