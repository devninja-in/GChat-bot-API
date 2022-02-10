package com.ggs.bot.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    private String id;
    @Column(name = "created_by",nullable = false,updatable = false)
    public String createdBy;
    @Column(name = "created_at")
    public Date createdAt;
    @Column(name = "updated_by")
    public String updatedBy;
    @Column(name = "updated_at")
    public Date updatedAt;

    public String getId() {
        return id;
    }

    public BaseEntity setId(String id) {
        this.id = id;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public BaseEntity setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public BaseEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public BaseEntity setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public BaseEntity setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof BaseEntity)) return false;

        BaseEntity that = (BaseEntity) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(createdBy, that.createdBy)
                .append(createdAt, that.createdAt)
                .append(updatedBy, that.updatedBy)
                .append(updatedAt, that.updatedAt)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(createdBy)
                .append(createdAt)
                .append(updatedBy)
                .append(updatedAt)
                .toHashCode();
    }
}
