package university.accommodation.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import university.accommodation.management.system.model.auth.Role;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByName(String name);
}
