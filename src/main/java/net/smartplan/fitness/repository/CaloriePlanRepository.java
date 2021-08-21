package net.smartplan.fitness.repository;

import net.smartplan.fitness.entity.CaloriePlan;
import org.springframework.data.repository.CrudRepository;

/**
 * @author H.D. Sachin Dilshan
 */

public interface CaloriePlanRepository extends CrudRepository<CaloriePlan, Integer> {

    CaloriePlan findByUserId_IdAndType(int id, String type);
}
