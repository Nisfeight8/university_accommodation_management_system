package university.accommodation.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import university.accommodation.management.system.model.Call;
import university.accommodation.management.system.model.University;

import java.util.List;

public interface CallRepository extends JpaRepository<Call,Integer> {
    List<Call> findByDepartmentUniversity(University university);
    @Query("Select c from Call c where c.start_date <= CURRENT_DATE and c.end_date >= CURRENT_DATE")
    <Optional> List<Call> findAllBetweenDates();
    boolean existsByTitle(String title);

}
