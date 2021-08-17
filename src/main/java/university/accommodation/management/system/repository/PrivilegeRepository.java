package university.accommodation.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import university.accommodation.management.system.model.auth.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege,Integer> {
}
