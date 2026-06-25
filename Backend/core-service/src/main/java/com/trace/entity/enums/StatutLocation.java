package com.trace.entity.enums;

public enum StatutLocation {
    LIBRE("Libre"),
    PARTIEL("Partiellement occupé"),
    OCCUPE("Occupé"),
    RESERVE("Réservé"),
    BLOQUE("Bloqué"),
    INACTIF("Inactif");

    private final String description;

    StatutLocation(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
