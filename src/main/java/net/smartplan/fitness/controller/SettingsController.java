package net.smartplan.fitness.controller;

import lombok.extern.slf4j.Slf4j;
import net.smartplan.fitness.dto.GeneralSystemSettingsDTO;
import net.smartplan.fitness.response.CommonResponse;
import net.smartplan.fitness.service.SystemSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/settings")
@CrossOrigin("*")
public class SettingsController {

    private final SystemSettingsService systemSettingsService;

    @Autowired
    public SettingsController(SystemSettingsService settingsService) {
        this.systemSettingsService = settingsService;
    }

    @GetMapping("/fetchAll")
    public List<GeneralSystemSettingsDTO> fetchAll() {
        return systemSettingsService.fetchAllSettings();
    }

    @PostMapping("/update")
    public GeneralSystemSettingsDTO update(@RequestBody GeneralSystemSettingsDTO systemSettingsDTO) {
        return systemSettingsService.update(systemSettingsDTO);
    }

    @GetMapping("/fetch/{setting}")
    public GeneralSystemSettingsDTO fetch(@PathVariable String setting) {
        return systemSettingsService.fetch(setting);
    }

    @PostMapping("/updateAll")
    public ResponseEntity<CommonResponse> saveAll(@RequestBody List<GeneralSystemSettingsDTO> generalSystemSettingsDTOList) {
        systemSettingsService.saveAll(generalSystemSettingsDTOList);
        return new ResponseEntity<>(new CommonResponse(true, "Updated!"), HttpStatus.OK);

    }
}
