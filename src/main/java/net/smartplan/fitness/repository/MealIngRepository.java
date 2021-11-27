package net.smartplan.fitness.repository;

import net.smartplan.fitness.entity.MealIngredients;
import org.springframework.data.repository.CrudRepository;


/**
 * @author H.D. Sachin Dilshan
 */

public interface MealIngRepository extends CrudRepository<MealIngredients, Integer> {

}
