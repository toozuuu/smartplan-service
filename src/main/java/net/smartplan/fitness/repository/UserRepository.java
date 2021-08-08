package net.smartplan.fitness.repository;

import net.smartplan.fitness.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

    List<User> findAll();
}
