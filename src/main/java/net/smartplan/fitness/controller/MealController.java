package net.smartplan.fitness.controller;

import lombok.extern.slf4j.Slf4j;
import net.smartplan.fitness.dto.MealDTO;
import net.smartplan.fitness.response.CommonResponse;
import net.smartplan.fitness.service.EmailService;
import net.smartplan.fitness.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meal")
@CrossOrigin
@Slf4j
public class MealController {

    private final MealService mealService;

    @Autowired
    public MealController(MealService mealService,
                          EmailService emailService) {
        this.mealService = mealService;
    }

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody MealDTO mealDTO) {

        try {
            mealService.save(mealDTO);
            return new ResponseEntity<>(new CommonResponse(true, "Success"), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(new CommonResponse(true, "Fail!"), HttpStatus.OK);
        }

    }

    @GetMapping("/allNew")
    public List<MealDTO> getAll() {
        return mealService.getAll();
    }

    @GetMapping("/getByUser/{id}")
    public List<MealDTO> getByUser(@PathVariable int id) {
        return mealService.getByUser(id);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable int id) {
        return mealService.delete(id);
    }

    @GetMapping("/preAndPost/{userId}")
    public List<MealDTO> getPreAndPostMeal(@PathVariable int userId) {
        return mealService.mealsByUserCalories(userId);
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody MealDTO mealDTO) {
        mealService.update(mealDTO);
        mealService.updateTotal(mealDTO.getId());
        return new ResponseEntity<>(new CommonResponse(true, "Success"), HttpStatus.OK);
    }

    @DeleteMapping("/ing/delete/{id}")
    public boolean deleteIng(@PathVariable int id) {
        return mealService.deleteIng(id);
    }

}
