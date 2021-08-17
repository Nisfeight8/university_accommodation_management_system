package university.accommodation.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import university.accommodation.management.system.model.Applicant;
import university.accommodation.management.system.model.Application;
import university.accommodation.management.system.model.Call;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Integer> {
    List<Application> findByCall(Call call);
    List<Application> findByApplicant(Applicant applicant);

}
