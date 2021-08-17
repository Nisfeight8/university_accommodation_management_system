package university.accommodation.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import university.accommodation.management.system.model.Admin;

public interface AdminRepository extends UserBaseRepository<Admin>, JpaRepository<Admin,Integer> {
}
