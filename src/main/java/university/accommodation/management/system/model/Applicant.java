package university.accommodation.management.system.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import university.accommodation.management.system.model.enums.Gender;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "user")
@Table(name = "applicants")
public class Applicant extends User {
    @NotEmpty
    @NotNull
    @Column(nullable = false, unique = true)
    String address;
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
    @Size(max = 10, min = 10, message = "Phone should be of 10 digits")
    @Pattern(regexp = "[0-9]+", message = "Phone is invalid!!")
    @Column(nullable = false, unique = true,length = 10)
    String phone;
    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    Date birth_date;
    @NotNull
    @Column(nullable = false)
    @Enumerated
    Gender gender;
    @NotEmpty
    @NotNull
    @Column(name = "first_name", nullable = false)
    String first_name;
    @NotEmpty
    @NotNull
    @Column(name = "last_name", nullable = false)
    String last_name;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "applicant",orphanRemoval=true,cascade=CascadeType.ALL)
    Collection<Application> applications;


}
