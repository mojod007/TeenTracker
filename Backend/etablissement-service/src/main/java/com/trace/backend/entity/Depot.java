package com.trace.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "depot", indexes = {
        @Index(name = "idx_depot_code", columnList = "code"),
        @Index(name = "idx_depot_etablissement_id", columnList = "etablissement_id"),
        @Index(name = "idx_depot_actif", columnList = "actif")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Depot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le code est obligatoire")
    @Size(min = 3, max = 50, message = "Le code doit contenir entre 3 et 50 caractères")
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false)
    private boolean actif = true;

    @Size(max = 200, message = "La localisation ne peut pas dépasser 200 caractères")
    @Column(length = 200)
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etablissement_id", nullable = false)
    @JsonBackReference
    private Etablissement etablissement;

    @OneToMany(mappedBy = "depot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Zone> zones;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
