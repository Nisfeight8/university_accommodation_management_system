package university.accommodation.management.system.repository;


import university.accommodation.management.system.model.User;

public interface UserRepository extends UserBaseRepository<User> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
}
