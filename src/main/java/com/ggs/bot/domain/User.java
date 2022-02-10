package com.ggs.bot.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "display_name") private String displayName;
    private String email;
    private String language;
    @Column(name = "gchat_user_id") private String gchatUserId;
    @Column(name = "gchat_space_id") private String gchatSpaceId;

    public String getDisplayName() {
        return displayName;
    }

    public User setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getGchatSpaceId() {
        return gchatSpaceId;
    }

    public User setGchatSpaceId(String spaceId) {
        this.gchatSpaceId = spaceId;
        return this;
    }

    public String getGchatUserId() {
        return gchatUserId;
    }

    public User setGchatUserId(String gchatUserId) {
        this.gchatUserId = gchatUserId;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public User setLanguage(String language) {
        this.language = language;
        return this;
    }
}
