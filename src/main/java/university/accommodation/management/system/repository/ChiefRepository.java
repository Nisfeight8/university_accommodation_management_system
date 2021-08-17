package university.accommodation.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import university.accommodation.management.system.model.Chief;

public interface ChiefRepository extends UserBaseRepository<Chief>, JpaRepository<Chief,Integer> {
    boolean existsByPhone(String phone);


}
