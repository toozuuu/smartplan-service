package net.smartplan.fitness.util;

import net.smartplan.fitness.dto.*;
import net.smartplan.fitness.entity.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author H.D. Sachin Dilshan
 */


@Service
public class ModelMapperUtil {

    private final ModelMapper modelMapper;

    @Autowired
    public ModelMapperUtil(ModelMapper modelMapper) {

        this.modelMapper = modelMapper;
    }

    public User convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public CaloriePlan convertToEntity(CaloriePlanDTO dto) {
        return modelMapper.map(dto, CaloriePlan.class);
    }

    public MacronutrientFood convertToEntity(MacronutrientFoodDTO dto) {
        return modelMapper.map(dto, MacronutrientFood.class);
    }

    public UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public CaloriePlanDTO convertToDTO(CaloriePlan caloriePlan) {
        return modelMapper.map(caloriePlan, CaloriePlanDTO.class);
    }

    public MacronutrientFoodDTO convertToDTO(MacronutrientFood macronutrientFood) {
        return modelMapper.map(macronutrientFood, MacronutrientFoodDTO.class);
    }

    public UserAddress convertToEntity(AddressDTO dto) {
        return modelMapper.map(dto, UserAddress.class);
    }

    public AddressDTO convertToDTO(UserAddress userAddress) {
        return modelMapper.map(userAddress, AddressDTO.class);
    }

    public MealDTO convertToDTO(Meal meal) {
        return modelMapper.map(meal, MealDTO.class);
    }

    public Meal convertToEntity(MealDTO mealDTO) {
        return modelMapper.map(mealDTO, Meal.class);
    }

    public IdentifyTrace convertToEntity(IdentifyTraceDTO identifyTraceDTO) {
        return modelMapper.map(identifyTraceDTO, IdentifyTrace.class);
    }

    public Purchase convertToEntity(PurchaseDTO purchaseDTO) {
        return modelMapper.map(purchaseDTO, Purchase.class);
    }

    public PurchaseDTO convertToDTO(Purchase purchase) {
        return modelMapper.map(purchase, PurchaseDTO.class);
    }

    public PurchaseDetails convertToEntity(PurchaseDetailsDTO purchaseDetailsDTO) {
        return modelMapper.map(purchaseDetailsDTO, PurchaseDetails.class);
    }

    public PurchaseDetailsDTO convertToDTO(PurchaseDetails purchaseDetails) {
        return modelMapper.map(purchaseDetails, PurchaseDetailsDTO.class);
    }

    public MealIngredients convertToEntity(MealIngredientsDTO mealIngredientsDTO) {
        return modelMapper.map(mealIngredientsDTO, MealIngredients.class);
    }

    public UnitType convertToEntity(UnitTypeDTO unitTypeDTO) {
        return modelMapper.map(unitTypeDTO, UnitType.class);
    }

    public UnitTypeDTO convertToDTO(UnitType unitType) {
        return modelMapper.map(unitType, UnitTypeDTO.class);
    }


    public Cart convertToEntity(CartDTO cartDTO) {
        return modelMapper.map(cartDTO, Cart.class);
    }

    public CartDTO convertToDTO(Cart cart) {
        return modelMapper.map(cart, CartDTO.class);
    }
}
