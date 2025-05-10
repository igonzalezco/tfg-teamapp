package com.unir.teamapp.persist.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public non-sealed class AuditableEntity extends BaseEntity implements Archivable{

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Override
    public void archive() {
        deletedAt = LocalDateTime.now();
    }

    @Override
    public void restore() {
        deletedAt = null;
    }

    
    /** 
     * @return boolean
     */
    @Override
    public boolean isArchived() {
        return deletedAt != null;
    }

}
