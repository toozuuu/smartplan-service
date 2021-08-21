package net.smartplan.fitness.repository;

import net.smartplan.fitness.entity.Meal;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author H.D. Sachin Dilshan
 */

public interface MealRepository extends CrudRepository<Meal, Integer> {

    List<Meal> findAllByStatusNotOrderByIdDesc(String stats);

    List<Meal> findAllByStatusNotAndMealIngredientsCollection_IngredientsNameIn(String status, List<String> foods);

    List<Meal> findAllByStatusNotAndTotalCarbsBetweenAndTotalFatBetweenAndTotalProteinBetweenAndMealIngredientsCollection_IngredientsNameIn(
          String status, double c1, double c2, double f1, double f2, double p1, double p2, List<String> foods);
}
