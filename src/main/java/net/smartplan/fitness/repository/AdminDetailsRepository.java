package net.smartplan.fitness.repository;

import net.smartplan.fitness.entity.AdminDetails;
import org.springframework.data.repository.CrudRepository;

/**
 * @author H.D. Sachin Dilshan
 */

public interface AdminDetailsRepository extends CrudRepository<AdminDetails, String> {

    AdminDetails findByEmail(String email);

}
