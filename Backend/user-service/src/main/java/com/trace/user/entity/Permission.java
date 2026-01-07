package com.trace.user.entity;

import lombok.*;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code; // USER_CREATE, PRODUCT_VIEW, etc.

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, length = 50)
    private String module; // USER, PRODUCT, ETABLISSEMENT, PROFILE

    @ManyToMany(mappedBy = "permissions")
    @Builder.Default
    private List<Profile> profiles = new ArrayList<>();
}
