package net.smartplan.fitness.repository;

import net.smartplan.fitness.entity.IdentifyTrace;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author H.D. Sachin Dilshan
 */

public interface IdentifyTraceRepository extends CrudRepository<IdentifyTrace, Integer> {

    List<IdentifyTrace> findAllByEmailAndStatus(String email, String status);
}
