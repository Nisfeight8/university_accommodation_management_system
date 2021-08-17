package university.accommodation.management.system.model.auth;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
@Data
@NoArgsConstructor
@Entity
@Table(name = "privileges")
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String name;
    @ManyToMany(mappedBy = "privileges")
    Collection<Role> roles;

    public Privilege(String name) {
        this.name = name;
    }
}
