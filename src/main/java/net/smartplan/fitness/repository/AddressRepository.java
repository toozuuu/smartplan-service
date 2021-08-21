package net.smartplan.fitness.repository;

import net.smartplan.fitness.entity.UserAddress;
import org.springframework.data.repository.CrudRepository;

/**
 * @author H.D. Sachin Dilshan
 */

public interface AddressRepository extends CrudRepository<UserAddress, Integer> {
}
