package university.accommodation.management.system.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import university.accommodation.management.system.model.enums.Status;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    @NotNull
    @Column(nullable = false)
    @Min(value = 0,message = "Must be positive number")
    int family_income;
    @NotNull
    @Column(nullable = false)
    @Min(value = 0,message = "Must be positive number")
    int siblings;
    @NotNull
    @Column(nullable = false)
    @Min(value = 0,message = "Must be positive number")
    int study_years;
    @NotNull
    @Column(nullable = false)
    @Min(value = 0,message = "Must be positive number")
    int accommodation_years;
    @Column(nullable = false)
    Date submission_date;
    @NotNull
    @Column(nullable = false)
    boolean working;
    @Column(columnDefinition = "varchar(9) default 'Pending' ", nullable = false)
    @Enumerated(value = EnumType.STRING)
    Status status=Status.Pending;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "call_id", referencedColumnName = "id")
    Call call;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", referencedColumnName = "user")
    Applicant applicant;
    @PrePersist
    private void onCreate() {
        submission_date = new Date();
    }
}
