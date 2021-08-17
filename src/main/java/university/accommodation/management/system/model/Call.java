package university.accommodation.management.system.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "calls")
public class Call {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    @NotEmpty
    @NotNull
    @Column(nullable = false,unique = true)
    String title;
    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    Date start_date;
    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    Date end_date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "call",cascade=CascadeType.ALL)
    private Collection<Application> applications;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "call")
    private Collection<Manager> managers;
    @PreRemove
    private void onDelete(){
        for (Manager manager : managers){
            manager.setCall(null);
        }
    }
}
