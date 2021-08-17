package university.accommodation.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import university.accommodation.management.system.model.Manager;
import university.accommodation.management.system.model.University;

import java.util.List;

public interface ManagerRepository extends UserBaseRepository<Manager>, JpaRepository<Manager,Integer> {

    List<Manager> findByUniversity(University university);
    boolean existsByPhone(String phone);
}
