package net.smartplan.fitness.repository;

import net.smartplan.fitness.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author H.D. Sachin Dilshan
 */

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByEmail(String email);

    List<User> findAll();
}
