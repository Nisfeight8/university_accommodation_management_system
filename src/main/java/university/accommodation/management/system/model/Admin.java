package university.accommodation.management.system.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "user")
@Table(name = "admins")
public class Admin extends User {

}
