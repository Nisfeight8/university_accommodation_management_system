package university.accommodation.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import university.accommodation.management.system.model.University;

import java.util.List;

public interface UniversityRepository extends JpaRepository<University,Integer> {
    List<University> findByChiefIsNull();
    boolean existsByName(String name);
    boolean existsByAddress(String address);
    boolean existsByPobox(String pobox);
    boolean existsByPhone(String phone);
}
