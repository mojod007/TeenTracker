package com.trace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.trace.entity.enums.TypeGestion;
import com.trace.entity.enums.TypeEmplacement;
import com.trace.entity.enums.StatutLocation;
import com.trace.entity.enums.TypePalette;
import java.time.LocalDateTime;

@Entity
@Table(name = "location", indexes = {
        @Index(name = "idx_location_code", columnList = "code"),
        @Index(name = "idx_location_zone_id", columnList = "zone_id"),
        @Index(name = "idx_location_actif", columnList = "actif"),
        @Index(name = "idx_location_type_gestion", columnList = "type_gestion"),
        @Index(name = "idx_location_statut", columnList = "statut"),
        @Index(name = "idx_location_type_emplacement", columnList = "type_emplacement"),
        @Index(name = "idx_location_code_barre", columnList = "code_barre"),
        @Index(name = "idx_location_allee", columnList = "allee")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le code est obligatoire")
    @Size(min = 3, max = 50, message = "Le code doit contenir entre 3 et 50 caracteres")
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @NotNull(message = "Le type de gestion est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(name = "type_gestion", nullable = false, length = 20)
    private TypeGestion typeGestion;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false)
    private boolean actif = true;

    @Size(max = 500, message = "La description ne peut pas depasser 500 caracteres")
    @Column(length = 500)
    private String description;

    @Positive(message = "La hauteur doit etre positive")
    @DecimalMax(value = "100.0", message = "La hauteur maximale est 100m")
    @Column(name = "height_meters")
    private Double height;

    @Positive(message = "La largeur doit etre positive")
    @DecimalMax(value = "100.0", message = "La largeur maximale est 100m")
    @Column(name = "width_meters")
    private Double width;

    @Positive(message = "La profondeur doit etre positive")
    @DecimalMax(value = "100.0", message = "La profondeur maximale est 100m")
    @Column(name = "depth_meters")
    private Double depth;

    @Positive(message = "Le poids maximum doit etre positif")
    @DecimalMax(value = "100000.0", message = "Le poids maximum est 100000kg")
    @Column(name = "max_weight_kg")
    private Double weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Zone zone;

    // --- Coordonnées logistiques ---

    @Size(max = 10, message = "L'allee ne peut pas depasser 10 caracteres")
    @Column(length = 10)
    private String allee;

    @Size(max = 10, message = "La colonne ne peut pas depasser 10 caracteres")
    @Column(length = 10)
    private String colonne;

    @Size(max = 10, message = "Le niveau ne peut pas depasser 10 caracteres")
    @Column(length = 10)
    private String niveau;

    @Column(name = "poste_profondeur")
    @Min(value = 1, message = "Le poste de profondeur minimum est 1")
    @Max(value = 9, message = "Le poste de profondeur maximum est 9")
    private Integer posteProfondeur;

    // --- Type d'emplacement ---

    @Enumerated(EnumType.STRING)
    @Column(name = "type_emplacement", length = 20)
    private TypeEmplacement typeEmplacement;

    // --- Capacités ---

    @Min(value = 0, message = "La capacite palettes ne peut pas etre negative")
    @Column(name = "capacite_palettes")
    private Integer capacitePalettes;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_palette_autorise", length = 20)
    private TypePalette typePaletteAutorise;

    @PositiveOrZero(message = "Le poids actuel doit etre positif ou zero")
    @Column(name = "poids_actuel_kg")
    private Double poidsActuelKg;

    // --- Statut & disponibilité ---

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "Le statut est obligatoire")
    private StatutLocation statut = StatutLocation.LIBRE;

    @Column(name = "derniere_occupation")
    private LocalDateTime derniereOccupation;

    @Column(nullable = false)
    private boolean bloque = false;

    // --- Identification code-barres ---

    @Size(max = 100, message = "Le code-barres ne peut pas depasser 100 caracteres")
    @Column(name = "code_barre", length = 100, unique = true)
    private String codeBarre;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Double calculateVolume() {
        if (height != null && width != null && depth != null) {
            return height * width * depth;
        }
        return null;
    }

    public boolean canHold(Double requiredWeight) {
        return weight != null && requiredWeight != null && weight >= requiredWeight;
    }
}
