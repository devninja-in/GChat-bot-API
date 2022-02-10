package com.ggs.bot.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Table: roles
 */
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Transient
    public static final String CONTENT_MANAGER = "content-manager";
    @Transient
    public static final String ADMIN = "admin";

    private String name;
    private Boolean isActive;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
