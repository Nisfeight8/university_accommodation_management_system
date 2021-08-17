package university.accommodation.management.system.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "user")
@Table(name = "managers")
public class Manager extends User{
    @NotEmpty
    @NotNull
    @Size(max = 10, min = 10, message = "Mobile number should be of 10 digits")
    @Pattern(regexp = "[0-9]+", message = "Mobile number is invalid!!")
    @Column(nullable = false, unique = true,length = 10)
    String phone;
    @ManyToOne
    @JoinColumn(name = "call_id", referencedColumnName = "id")
    Call call;
    @ManyToOne
    @JoinColumn(name = "university_id", referencedColumnName = "id")
    University university;
}
