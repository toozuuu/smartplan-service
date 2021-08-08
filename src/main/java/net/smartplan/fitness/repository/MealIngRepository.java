package net.smartplan.fitness.repository;

import net.smartplan.fitness.entity.MealIngredients;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MealIngRepository extends CrudRepository<MealIngredients, Integer> {

    List<MealIngredients> findAllByMealId_Id(int id);
}
