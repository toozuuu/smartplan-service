package net.smartplan.fitness.service;

import net.smartplan.fitness.dto.MealDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MealService {

    List<MealDTO> getAll();

    List<MealDTO> getByUser(int id);

    MealDTO save(MealDTO mealDTO);

    boolean delete(int id);

    List<MealDTO> mealsByUserCalories(int id);

    ResponseEntity update(MealDTO mealDTO);

    void updateTotal(int id);

    boolean deleteIng(int id);

}
