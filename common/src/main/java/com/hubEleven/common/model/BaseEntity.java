package com.hubEleven.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false, nullable = false)
    @Comment("생성일")
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(updatable = false)
    @Comment("생성자")
    private Long createdBy;

    @LastModifiedDate
    @Comment("수정일")
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Comment("수정자")
    private Long updatedBy;

    @Comment("삭제일")
    private LocalDateTime deletedAt;

    @Comment("삭제자")
    private Long deletedBy;

    public void delete(Long deletedBy) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
