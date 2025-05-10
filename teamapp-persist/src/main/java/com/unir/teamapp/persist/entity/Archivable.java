package com.unir.teamapp.persist.entity;

public sealed interface Archivable permits AuditableEntity {

    void archive();

    void restore();

    boolean isArchived();
}
