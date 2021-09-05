package net.smartplan.fitness.repository;

import net.smartplan.fitness.entity.GeneralSystemSettings;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author H.D. Sachin Dilshan
 */

public interface SystemSettingsRepository extends CrudRepository<GeneralSystemSettings, Integer> {

    List<GeneralSystemSettings> findAll();

    GeneralSystemSettings findByCode(String code);

}
