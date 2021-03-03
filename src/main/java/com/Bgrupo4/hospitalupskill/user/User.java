package com.Bgrupo4.hospitalupskill.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.sql.Date;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity(name = "user")
public class User implements UserDetails {

    @Id
    private Integer nif;
    private String name;
    private String username;
    private String email;
    private String password;
    private String morada;
    private String localidade;
    private String phone;
    private Date birthday;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    private Boolean locked = false;
    private Boolean enabled = false;
    private String profilePicture = "";


    //Test purposes


    public User(Integer nif, String name) {
        this.nif = nif;
        this.name = name;
    }

    public User(Integer nif,
                String name,
                String username,
                String email,
                String password) {
        this.nif = nif;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(Integer nif,
                String name,
                String username,
                String email,
                String password,
                String morada,
                String localidade,
                String phone,
                Date birthday,
                UserRole userRole) {
        this.nif = nif;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.morada = morada;
        this.localidade = localidade;
        this.phone = phone;
        this.birthday = birthday;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "nif=" + nif +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", morada='" + morada + '\'' +
                ", localidade='" + localidade + '\'' +
                ", phone='" + phone + '\'' +
                ", birthday=" + birthday +
                //", userRole=" + userRole +
                ", locked=" + locked +
                ", enabled=" + enabled +
                ", profilePicture='" + profilePicture + '\'' +
                '}';
    }
}
