package net.smartplan.fitness.service;

import net.smartplan.fitness.dto.UnitTypeDTO;

import java.util.List;

/**
 * @author H.D. Sachin Dilshan
 */

public interface UnitTypeService {

    List<UnitTypeDTO> getAll();

    void update(UnitTypeDTO unitTypeDTO);

    UnitTypeDTO save(UnitTypeDTO unitTypeDTO);

    boolean delete(int id);
}
