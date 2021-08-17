package university.accommodation.management.system.model;

import javax.persistence.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    @NotEmpty
    @NotNull
    @Size(max = 30, min = 3, message = "Name must be between 3 and 30 characters !")
    @Column(nullable = false, unique = true)
    String name;
    @NotEmpty
    @NotNull
    @Column(nullable = false, unique = true)
    String address;
    @NotEmpty
    @NotNull
    @Size(max =5, min = 5, message = "Pobox must have 5 digits")
    @Column(nullable = false, unique = true)
    @Pattern(regexp = "[0-9]+", message = "Pobox is invalid!!")
    String pobox;
    @NotEmpty
    @NotNull
    @Column(nullable = false)
    String city;
    @NotEmpty
    @NotNull
    @Column(nullable = false)
    String country;
    @NotEmpty
    @NotNull
    @Size(max = 10, min = 10, message = "Mobile number should be of 10 digits")
    @Pattern(regexp = "[0-9]+", message = "Mobile number is invalid!!")
    @Column(nullable = false, unique = true,length = 10)
    String phone;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", referencedColumnName = "id")
    private University university;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "department",cascade=CascadeType.ALL)
    private Collection<Call> calls;

}
