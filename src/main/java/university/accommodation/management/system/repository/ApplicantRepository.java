package university.accommodation.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import university.accommodation.management.system.model.Applicant;

public interface ApplicantRepository extends UserBaseRepository<Applicant>, JpaRepository<Applicant,Integer> {
    boolean existsByAddress(String address);
    boolean existsByPhone(String phone);

}

