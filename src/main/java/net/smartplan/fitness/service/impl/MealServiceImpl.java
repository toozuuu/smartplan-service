package net.smartplan.fitness.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.smartplan.fitness.dto.MealDTO;
import net.smartplan.fitness.dto.MealIngredientsDTO;
import net.smartplan.fitness.entity.CaloriePlan;
import net.smartplan.fitness.entity.Meal;
import net.smartplan.fitness.entity.MealIngredients;
import net.smartplan.fitness.entity.User;
import net.smartplan.fitness.repository.CaloriePlanRepository;
import net.smartplan.fitness.repository.MealIngRepository;
import net.smartplan.fitness.repository.MealRepository;
import net.smartplan.fitness.repository.UserRepository;
import net.smartplan.fitness.response.CommonResponse;
import net.smartplan.fitness.service.MealService;
import net.smartplan.fitness.util.ModelMapperUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(rollbackOn = Exception.class)
@Slf4j
public class MealServiceImpl implements MealService {


    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final ModelMapperUtil modelMapperUtil;
    private final MealIngRepository ingRepository;
    private final CaloriePlanRepository caloriePlanRepository;
    private static final String DISABLED = "DISABLED";

    @Autowired
    public MealServiceImpl(MealRepository mealRepository,
                           UserRepository userRepository,
                           ModelMapperUtil modelMapperUtil,
                           MealIngRepository ingRepository,
                           CaloriePlanRepository caloriePlanRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
        this.modelMapperUtil = modelMapperUtil;
        this.ingRepository = ingRepository;
        this.caloriePlanRepository = caloriePlanRepository;
    }

    @Override
    public List<MealDTO> getAll() {
        return mealRepository.findAllByStatusNotOrderByIdDesc(DISABLED).stream().map(modelMapperUtil::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<MealDTO> getByUser(int id) {

        Optional<User> user = userRepository.findById(id);
        List<String> foods = new ArrayList<>();
        user.ifPresent(value -> value.getMacronutrientFoodCollection().forEach(detail -> foods.add(detail.getFood())));
        List<Meal> meals = mealRepository.findAllByStatusNotAndMealIngredientsCollection_IngredientsNameIn(DISABLED, foods);
        return meals.stream().distinct().map(modelMapperUtil::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public MealDTO save(MealDTO mealDTO) {

        double totalProtein = 0;
        double totalCarbs = 0;
        double totalFat = 0;

        Meal meal = mealRepository.save(modelMapperUtil.convertToEntity(mealDTO));
        for (MealIngredientsDTO dto : mealDTO.getMealIngredientsCollection()) {
            MealIngredients mealIngredients = modelMapperUtil.convertToEntity(dto);
            mealIngredients.setMealId(meal);
            totalProtein += dto.getProtein();
            totalFat += dto.getFat();
            totalCarbs += dto.getCarbs();
            ingRepository.save(mealIngredients);
        }
        meal.setTotalCarbs(totalCarbs);
        meal.setTotalFat(totalFat);
        meal.setTotalProtein(totalProtein);

        return modelMapperUtil.convertToDTO(mealRepository.save(meal));
    }

    @Override
    public boolean delete(int id) {

        Optional<Meal> optional = mealRepository.findById(id);
        if (optional.isPresent()) {
            Meal meal = optional.get();
            meal.setStatus(DISABLED);
            mealRepository.save(meal);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<MealDTO> mealsByUserCalories(int id) {

        Optional<User> user = userRepository.findById(id);
        List<String> foods = new ArrayList<>();
        user.ifPresent(value -> value.getMacronutrientFoodCollection().forEach(detail -> foods.add(detail.getFood())));

        CaloriePlan nonCaloriePlan = caloriePlanRepository.findByUserId_IdAndType(id, "non-workout");
        CaloriePlan caloriePlan = caloriePlanRepository.findByUserId_IdAndType(id, "workout");

        double fat = caloriePlan.getFat() / caloriePlan.getMealsPerDay();
        double carbs = caloriePlan.getCarbs() / caloriePlan.getMealsPerDay();
        double pro = caloriePlan.getProtein() / caloriePlan.getMealsPerDay();

        double nonFat = nonCaloriePlan.getFat() / caloriePlan.getMealsPerDay();
        double nonCarbs = nonCaloriePlan.getCarbs() / caloriePlan.getMealsPerDay();
        double nonPro = nonCaloriePlan.getProtein() / caloriePlan.getMealsPerDay();

        List<Meal> workOutMeals = mealRepository.findAllByStatusNotAndTotalCarbsBetweenAndTotalFatBetweenAndTotalProteinBetweenAndMealIngredientsCollection_IngredientsNameIn
                (DISABLED, carbs - 5, carbs + 5, fat - 5, fat + 5, pro - 5, pro + 5, foods);

        List<Meal> nonWorkOutMeals = mealRepository.findAllByStatusNotAndTotalCarbsBetweenAndTotalFatBetweenAndTotalProteinBetweenAndMealIngredientsCollection_IngredientsNameIn
                (DISABLED, nonCarbs - 5, nonCarbs + 5, nonFat - 5, nonFat + 5, nonPro - 5, nonPro + 5, foods);

        List<Meal> finalList = Stream.concat(workOutMeals.stream(), nonWorkOutMeals.stream()).collect(Collectors.toList());

        return finalList.stream().distinct().map(modelMapperUtil::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void update(MealDTO mealDTO) {

        Meal meal = mealRepository.save(modelMapperUtil.convertToEntity(mealDTO));
        mealDTO.getMealIngredientsCollection().forEach(ing -> {
            MealIngredients mealIngredients = modelMapperUtil.convertToEntity(ing);
            mealIngredients.setMealId(meal);
            ingRepository.save(mealIngredients);
        });
        new ResponseEntity<>(new CommonResponse(true, "Success"), HttpStatus.OK);
    }

    public void updateTotal(int id) {
        Optional<Meal> optional = mealRepository.findById(id);
        if (optional.isPresent()) {
            Meal meal = optional.get();
            double totalProtein = 0;
            double totalCarbs = 0;
            double totalFat = 0;

            for (MealIngredients ing : meal.getMealIngredientsCollection()) {
                totalProtein += ing.getProtein();
                totalFat += ing.getFat();
                totalCarbs += ing.getCarbs();
            }
            meal.setTotalCarbs(totalCarbs);
            meal.setTotalFat(totalFat);
            meal.setTotalProtein(totalProtein);
            modelMapperUtil.convertToDTO(mealRepository.save(meal));
        }

    }

    @Override
    public boolean deleteIng(int id) {

        Optional<MealIngredients> optional = ingRepository.findById(id);
        if (optional.isPresent()) {
            ingRepository.delete(optional.get());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ByteArrayInputStream getMealReport() {
        String[] columns = {"Meal Id", "Meal Name", "Meal Type", "Price($)", "Status", "Created", "Updated"};

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("Meal");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        Row headerRow = sheet.createRow(0);
        for (int col = 0; col < columns.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(columns[col]);
            cell.setCellStyle(headerCellStyle);
        }
        CellStyle ageCellStyle = workbook.createCellStyle();
        ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));
        List<MealDTO> list = getAll();
        int rowIdx = 1;
        for (MealDTO dto : list) {
            Row row = sheet.createRow(rowIdx++);
            DateFormat formatter = new SimpleDateFormat("dd-M-yyyy");

            row.createCell(0).setCellValue(dto.getId());
            row.createCell(1).setCellValue(dto.getMealName());
            row.createCell(2).setCellValue(dto.getMealType());
            row.createCell(3).setCellValue(dto.getPrice());
            row.createCell(4).setCellValue(dto.getStatus());
            row.createCell(5).setCellValue(formatter.format(dto.getCreated()));
            row.createCell(6).setCellValue(formatter.format(dto.getUpdated()));

        }

        try {
            workbook.write(out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
}
