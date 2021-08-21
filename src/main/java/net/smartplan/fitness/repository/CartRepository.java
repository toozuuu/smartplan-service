package net.smartplan.fitness.repository;

import net.smartplan.fitness.entity.Cart;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author H.D. Sachin Dilshan
 */

public interface CartRepository extends CrudRepository<Cart,Integer> {

    List<Cart> findAllByEmail(String email);

    Cart findByEmailAndMealId_Id(String email, int id);
}
