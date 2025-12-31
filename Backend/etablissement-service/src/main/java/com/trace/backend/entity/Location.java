package com.trace.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.trace.backend.enums.TypeGestion;
import java.time.LocalDateTime;

@Entity
@Table(name = "location", indexes = {
        @Index(name = "idx_location_code", columnList = "code"),
        @Index(name = "idx_location_zone_id", columnList = "zone_id"),
        @Index(name = "idx_location_actif", columnList = "actif"),
        @Index(name = "idx_location_type_gestion", columnList = "type_gestion")
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
    @Size(min = 3, max = 50, message = "Le code doit contenir entre 3 et 50 caractères")
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @NotNull(message = "Le type de gestion est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(name = "type_gestion", nullable = false, length = 20)
    private TypeGestion typeGestion;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false)
    private boolean actif = true;

    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    @Column(length = 500)
    private String description;

    @Positive(message = "La hauteur doit être positive")
    @DecimalMax(value = "100.0", message = "La hauteur maximale est 100m")
    @Column(name = "height_meters")
    private Double height;

    @Positive(message = "La largeur doit être positive")
    @DecimalMax(value = "100.0", message = "La largeur maximale est 100m")
    @Column(name = "width_meters")
    private Double width;

    @Positive(message = "La profondeur doit être positive")
    @DecimalMax(value = "100.0", message = "La profondeur maximale est 100m")
    @Column(name = "depth_meters")
    private Double depth;

    @Positive(message = "Le poids maximum doit être positif")
    @DecimalMax(value = "100000.0", message = "Le poids maximum est 100000kg")
    @Column(name = "max_weight_kg")
    private Double weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    @JsonBackReference
    private Zone zone;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Méthodes utilitaires
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
