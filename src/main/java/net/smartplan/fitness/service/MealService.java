package net.smartplan.fitness.service;

import net.smartplan.fitness.dto.MealDTO;

import java.io.ByteArrayInputStream;
import java.util.List;


/**
 * @author H.D. Sachin Dilshan
 */


public interface MealService {

    List<MealDTO> getAll();

    List<MealDTO> getByUser(int id);

    MealDTO save(MealDTO mealDTO);

    boolean delete(int id);

    List<MealDTO> mealsByUserCalories(int id);

    void update(MealDTO mealDTO);

    void updateTotal(int id);

    boolean deleteIng(int id);

    ByteArrayInputStream getMealReport();


}
