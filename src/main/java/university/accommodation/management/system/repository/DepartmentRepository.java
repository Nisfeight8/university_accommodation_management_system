package university.accommodation.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import university.accommodation.management.system.model.Department;
import university.accommodation.management.system.model.University;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department,Integer> {
    List<Department> findByUniversity(University university);
    boolean existsByName(String name);
    boolean existsByAddress(String address);
    boolean existsByPobox(String pobox);
    boolean existsByPhone(String phone);



}

