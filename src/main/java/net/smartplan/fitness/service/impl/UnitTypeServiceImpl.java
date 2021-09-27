package net.smartplan.fitness.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.smartplan.fitness.dto.UnitTypeDTO;
import net.smartplan.fitness.entity.UnitType;
import net.smartplan.fitness.repository.UnitTypeRepository;
import net.smartplan.fitness.service.UnitTypeService;
import net.smartplan.fitness.util.ModelMapperUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UnitTypeServiceImpl implements UnitTypeService {

    private final UnitTypeRepository unitTypeRepository;
    private final ModelMapperUtil modelMapperUtil;

    public UnitTypeServiceImpl(UnitTypeRepository unitTypeRepository, ModelMapperUtil modelMapperUtil) {
        this.unitTypeRepository = unitTypeRepository;
        this.modelMapperUtil = modelMapperUtil;
    }

    @Override
    public List<UnitTypeDTO> getAll() {
        Iterable<UnitType> all = this.unitTypeRepository.findAll();
        List<UnitTypeDTO> unitTypeDTOS = new ArrayList<>();
        all.forEach(unitType -> {
            UnitTypeDTO unitTypeDTO = new UnitTypeDTO();
            unitTypeDTO.setId(unitType.getId());
            unitTypeDTO.setCreated(unitType.getCreated());
            unitTypeDTO.setDescription(unitType.getDescription());
            unitTypeDTO.setUpdated(unitType.getUpdated());
            unitTypeDTOS.add(unitTypeDTO);
        });
        return unitTypeDTOS;
    }

    @Override
    public void update(UnitTypeDTO unitTypeDTO) {
        Optional<UnitType> optional = unitTypeRepository.findById(unitTypeDTO.getId());
        if (optional.isPresent()) {
            UnitType unitType = optional.get();
            unitType.setId(unitType.getId());
            unitType.setDescription(unitTypeDTO.getDescription());
            this.unitTypeRepository.save(unitType);
        }
    }

    @Override
    public UnitTypeDTO save(UnitTypeDTO unitTypeDTO) {
        UnitType save = this.unitTypeRepository.save(this.modelMapperUtil.convertToEntity(unitTypeDTO));
        return this.modelMapperUtil.convertToDTO(save);
    }

    @Override
    public boolean delete(int id) {
        Optional<UnitType> optional = unitTypeRepository.findById(id);
        if (optional.isPresent()) {
            unitTypeRepository.delete(optional.get());
            return true;
        } else {
            return false;
        }
    }
}
