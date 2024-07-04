package com.spring.auth.entity;

import com.spring.auth.common.RoleType;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "\"user\"")
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER")
    @SequenceGenerator(name = "USER", sequenceName = "SEQ_OT_USER", allocationSize = 1)
    private Long id;

    private String userName;
    private String email;
    private String phoneNo;
    private String dateOfBirth;
    private String gender;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;

    @Enumerated(value = EnumType.STRING)
    private RoleType role;

    private String password;

    // Constructors
    public User() {
    }

    public User(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.phoneNo = user.getPhoneNo();
        this.dateOfBirth = user.getDateOfBirth();
        this.gender = user.getGender();
        this.enabled = user.isEnabled();
        this.accountNonExpired = user.isAccountNonExpired();
        this.credentialsNonExpired = user.isCredentialsNonExpired();
        this.accountNonLocked = user.isAccountNonLocked();
        this.role = user.getRole();
        this.password = user.getPassword();
    }

    // Equals and HashCode based on id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
