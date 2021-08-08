package net.smartplan.fitness.repository;

import net.smartplan.fitness.entity.Meal;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MealRepository extends CrudRepository<Meal, Integer> {

    List<Meal> findAllByStatusNotOrderByIdDesc(String stats);

    List<Meal> findAllByStatusNotAndMealIngredientsCollection_IngredientsNameIn(String status, List<String> foods);

//    List<Meal> findAllByTotalCarbsBetweenAndTotalFatBetweenAndTotalProteinBetweenAndMealTypeAndMealIngredientsCollection_IngredientsNameIn(
//            double carbs1, double carbs2,double fat1, double fat2,double pro1, double pro2,List<String> foods);

    List<Meal> findAllByStatusNotAndTotalCarbsBetweenAndTotalFatBetweenAndTotalProteinBetweenAndMealIngredientsCollection_IngredientsNameIn(
          String status, double c1, double c2, double f1, double f2, double p1, double p2, List<String> foods);
}
