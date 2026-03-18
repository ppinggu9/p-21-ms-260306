package com.back.site.user.entitiy;

import com.back.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class SiteUser extends BaseEntity {

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    public SiteUser(String username, String email,String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public void update(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

}
