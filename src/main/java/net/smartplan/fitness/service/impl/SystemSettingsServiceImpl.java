package net.smartplan.fitness.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.smartplan.fitness.dto.GeneralSystemSettingsDTO;
import net.smartplan.fitness.entity.GeneralSystemSettings;
import net.smartplan.fitness.repository.SystemSettingsRepository;
import net.smartplan.fitness.service.SystemSettingsService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
public class SystemSettingsServiceImpl implements SystemSettingsService {

    private final SystemSettingsRepository systemSettingsRepository;
    private final ModelMapper modelMapper;

    public SystemSettingsServiceImpl(SystemSettingsRepository systemSettingsRepository, ModelMapper modelMapper) {
        this.systemSettingsRepository = systemSettingsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<GeneralSystemSettingsDTO> fetchAllSettings() {
        List<GeneralSystemSettings> settings = systemSettingsRepository.findAll();
        if (settings != null) {
            return settings.stream().map(this::convertToDTO).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public GeneralSystemSettingsDTO fetch(String code) {
        return convertToDTO(systemSettingsRepository.findByCode(code));
    }

    @Override
    public GeneralSystemSettingsDTO update(GeneralSystemSettingsDTO systemSettingsDTO) {
        return convertToDTO(systemSettingsRepository.save(convertToEntity(systemSettingsDTO)));
    }

    @Override
    public void saveAll(List<GeneralSystemSettingsDTO> generalSystemSettingsDTOList) {

        List<GeneralSystemSettingsDTO> generalSystemSettingsDTOS = new ArrayList<>();
        for (GeneralSystemSettingsDTO generalSystemSettingsDTO : generalSystemSettingsDTOList) {
            generalSystemSettingsDTOS.add(convertToDTO(systemSettingsRepository.save(convertToEntity(generalSystemSettingsDTO))));
        }
    }

    private GeneralSystemSettings convertToEntity(GeneralSystemSettingsDTO settingsDTO) {
        return modelMapper.map(settingsDTO, GeneralSystemSettings.class);
    }

    private GeneralSystemSettingsDTO convertToDTO(GeneralSystemSettings systemSettings) {
        return modelMapper.map(systemSettings, GeneralSystemSettingsDTO.class);
    }
}
