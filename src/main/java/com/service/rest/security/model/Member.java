package com.service.rest.security.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "rest", name = "Members")
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotNull
    private String address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="social_id")
    private SocialData social;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = "rest", name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    // @Column(name = "status")
    // @Enumerated(EnumType.STRING)
    // private UserStatus status = UserStatus.NONBLOCKED;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createAt;
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "salt_id")
    private Salt salt;

    public Member() {
    }

    public Member(@NotBlank String username, @NotBlank String password, @NotBlank String name, @NotBlank String email, @NotBlank String address) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "seq=" + id +
                ", id='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
