package net.smartplan.fitness.controller;

import lombok.extern.slf4j.Slf4j;
import net.smartplan.fitness.dto.UnitTypeDTO;
import net.smartplan.fitness.response.CommonResponse;
import net.smartplan.fitness.service.UnitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author H.D. Sachin Dilshan
 */

@RestController
@RequestMapping("/unitType")
@CrossOrigin
@Slf4j

public class UnitTypeController {

    private final UnitTypeService unitTypeService;

    @Autowired
    public UnitTypeController(UnitTypeService unitTypeService) {
        this.unitTypeService = unitTypeService;
    }

    @PostMapping("/save")
    public ResponseEntity<CommonResponse> save(@RequestBody UnitTypeDTO unitTypeDTO) {

        try {
            unitTypeService.save(unitTypeDTO);
            return new ResponseEntity<>(new CommonResponse(true, "Success"), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(new CommonResponse(true, "Fail!"), HttpStatus.OK);
        }

    }

    @GetMapping("/all")
    public List<UnitTypeDTO> getAll() {
        return unitTypeService.getAll();
    }


    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable int id) {
        return unitTypeService.delete(id);
    }

    @PostMapping("/update")
    public ResponseEntity<CommonResponse> update(@RequestBody UnitTypeDTO unitTypeDTO) {
        unitTypeService.update(unitTypeDTO);
        return new ResponseEntity<>(new CommonResponse(true, "Success"), HttpStatus.OK);
    }


}
