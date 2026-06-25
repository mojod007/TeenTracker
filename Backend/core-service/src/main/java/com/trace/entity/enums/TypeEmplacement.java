package com.trace.entity.enums;

public enum TypeEmplacement {
    PICKING("Picking"),
    RESERVE("Réserve"),
    RECEPTION("Réception"),
    EXPEDITION("Expédition"),
    QUARANTAINE("Quarantaine"),
    CROSS_DOCK("Cross-dock"),
    BULK("Vrac");

    private final String description;

    TypeEmplacement(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
