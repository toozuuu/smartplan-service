package net.smartplan.fitness.repository;

import net.smartplan.fitness.entity.Purchase;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author H.D. Sachin Dilshan
 */

public interface PurchaseRepository extends CrudRepository<Purchase, Integer> {

    List<Purchase> findAllByEmailOrderByCreatedDesc(String email);

}
