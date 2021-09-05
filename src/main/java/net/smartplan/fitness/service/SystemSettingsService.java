package net.smartplan.fitness.service;


import net.smartplan.fitness.dto.GeneralSystemSettingsDTO;

import java.util.List;

/**
 * @author H.D. Sachin Dilshan
 */

public interface SystemSettingsService {

    List<GeneralSystemSettingsDTO> fetchAllSettings();

    GeneralSystemSettingsDTO fetch(String code);

    GeneralSystemSettingsDTO update(GeneralSystemSettingsDTO systemSettingsDTO);

    void saveAll(List<GeneralSystemSettingsDTO> generalSystemSettingsDTOList);
}
