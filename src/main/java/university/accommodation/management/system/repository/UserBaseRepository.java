package university.accommodation.management.system.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface UserBaseRepository<T>
        extends CrudRepository<T, Integer> {
    @Override
    Optional<T> findById(Integer id);

    @Override
    List<T> findAll();
}

