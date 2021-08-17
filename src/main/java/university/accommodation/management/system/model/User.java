package university.accommodation.management.system.model;

import java.util.Collection;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import university.accommodation.management.system.model.auth.Role;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
@Component
public abstract class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    @NotEmpty
    @NotNull
    @Column(nullable = false, unique = true)
    String username;
    @NotEmpty
    @NotNull
    @Column(name = "password",nullable = false)
    String password;
    @Transient
    private String matchingPassword;
    @Column(columnDefinition = "boolean default true", nullable = false)
    boolean enabled = true;
    @Transient
    public Collection<? extends GrantedAuthority> authorities;
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    Collection<Role> roles;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @PreRemove
    private void OnDelete(){
        setRoles(null);
    }
}
