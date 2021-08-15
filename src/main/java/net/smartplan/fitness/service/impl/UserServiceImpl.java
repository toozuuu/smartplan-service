package net.smartplan.fitness.service.impl;

import net.smartplan.fitness.dto.*;
import net.smartplan.fitness.entity.CaloriePlan;
import net.smartplan.fitness.entity.MacronutrientFood;
import net.smartplan.fitness.entity.User;
import net.smartplan.fitness.entity.UserAddress;
import net.smartplan.fitness.repository.AddressRepository;
import net.smartplan.fitness.repository.CaloriePlanRepository;
import net.smartplan.fitness.repository.MacronutrientFoodRepository;
import net.smartplan.fitness.repository.UserRepository;
import net.smartplan.fitness.service.UserService;
import net.smartplan.fitness.util.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = Exception.class)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CaloriePlanRepository caloriePlanRepository;
    private final MacronutrientFoodRepository macronutrientFoodRepository;
    private final ModelMapperUtil modelMapperUtil;
    private final AddressRepository addressRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           CaloriePlanRepository caloriePlanRepository,
                           MacronutrientFoodRepository macronutrientFoodRepository,
                           ModelMapperUtil modelMapperUtil,
                           AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.caloriePlanRepository = caloriePlanRepository;
        this.macronutrientFoodRepository = macronutrientFoodRepository;
        this.modelMapperUtil = modelMapperUtil;
        this.addressRepository = addressRepository;
    }

    @Override
    public UserDTO register(UserDTO userDTO) {

        User user = userRepository.save(modelMapperUtil.convertToEntity(userDTO));
        UserAddress address = modelMapperUtil.convertToEntity(userDTO.getAddress());
        address.setUserId(user);
        addressRepository.save(address);
        userDTO.getCaloriePlanList().forEach(detail -> {
            CaloriePlan caloriePlan = modelMapperUtil.convertToEntity(detail);
            caloriePlan.setUserId(user);
            caloriePlanRepository.save(caloriePlan);
        });
        userDTO.getMacronutrientFoodList().forEach(detail -> {
            MacronutrientFood food = modelMapperUtil.convertToEntity(detail);
            food.setUserId(user);
            macronutrientFoodRepository.save(food);
        });
        UserDTO dto = modelMapperUtil.convertToDTO(user);
        dto.setAddress(modelMapperUtil.convertToDTO(address));
        return dto;
    }

    @Override
    public boolean checkEmail(String email) {

        User user = userRepository.findByEmail(email);
        return user != null;
    }

    @Override
    public UserDTO login(UserDTO userDTO) {
        userDTO.setCreated(new Date());
        User user = userRepository.findByEmailAndPassword(userDTO.getEmail(), userDTO.getPassword());
        if (user != null && user.getStatus().equals("ACTIVE")) {
            long numOfDays = new Date().getTime() - user.getCreated().getTime();
            List<CaloriePlanDTO> caloriePlans = user.getCaloriePlanCollection().stream().map(modelMapperUtil::convertToDTO).
                    collect(Collectors.toList());
            List<MacronutrientFoodDTO> macronutrientFoodDTOS = user.getMacronutrientFoodCollection().stream().
                    map(modelMapperUtil::convertToDTO).collect(Collectors.toList());
            UserDTO dto = modelMapperUtil.convertToDTO(user);
            dto.setCaloriePlanList(caloriePlans);
            dto.setNumOfDays((numOfDays / (1000 * 60 * 60 * 24)) % 365);
            dto.setSuccess(true);
            dto.setAddress(modelMapperUtil.convertToDTO(user.getUserAddressCollection().get(0)));
            dto.setMacronutrientFoodList(macronutrientFoodDTOS);
            return dto;
        } else {
            UserDTO dto = new UserDTO();
            dto.setSuccess(false);
            return dto;
        }


    }

    @Override
    public AddressDTO updateAddress(AddressDTO addressDTO) {

        Optional<UserAddress> optional = addressRepository.findById(addressDTO.getId());
        if (optional.isPresent()) {
            UserAddress userAddress = optional.get();
            userAddress.setAdrdess(addressDTO.getAdrdess());
            return modelMapperUtil.convertToDTO(addressRepository.save(userAddress));
        } else {
            return new AddressDTO();
        }

    }

    @Override
    public UpdateUserStatusDTO updateUserStatus(UpdateUserStatusDTO updateUserStatusDTO) {
        User user = userRepository.findByEmail(updateUserStatusDTO.getEmail());
        if (user != null) {
            user.setStatus(updateUserStatusDTO.getStatus());
            userRepository.save(user);
            return updateUserStatusDTO;
        } else {
            return new UpdateUserStatusDTO();
        }
    }

    @Override
    public void updateUser(UserDTO userDTO) {

        Optional<User> optional = userRepository.findById(userDTO.getId());
        if (optional.isPresent()) {

            User user = optional.get();
            user.setAge(userDTO.getAge());
            user.setWeight(userDTO.getWeight());
            user.setGoalType(userDTO.getGoalType());
            user.setGoalTime(userDTO.getGoalTime());
            updateDetails(userDTO, user);
            if (!userDTO.getMacronutrientFoodList().isEmpty()) {
                macronutrientFoodRepository.deleteAll(user.getMacronutrientFoodCollection());
                updateMacronutrientFood(userDTO.getMacronutrientFoodList(), user);
            }
            if ((!userDTO.getCaloriePlanList().isEmpty())) {
                caloriePlanRepository.deleteAll(user.getCaloriePlanCollection());
                updateCaloriePlan(userDTO.getCaloriePlanList(), user);
            }
            modelMapperUtil.convertToDTO(user);

        }
    }

    @Override
    public void recalculate(UserDTO userDTO) {
        Optional<User> optional = userRepository.findById(userDTO.getId());
        if (optional.isPresent()) {

            User user = optional.get();
            user.setAge(userDTO.getAge());
            user.setWeight(userDTO.getWeight());
            updateDetails(userDTO, user);

            if ((!userDTO.getCaloriePlanList().isEmpty())) {
                caloriePlanRepository.deleteAll(user.getCaloriePlanCollection());
                updateCaloriePlan(userDTO.getCaloriePlanList(), user);
            }
            modelMapperUtil.convertToDTO(user);
        }
    }

    @Override
    public List<UserDTO> getAll() {

        List<User> all = userRepository.findAll();
        List<UserDTO> results = new ArrayList<>();
        all.forEach(user -> {
            UserDTO dto = modelMapperUtil.convertToDTO(user);
            List<CaloriePlanDTO> caloriePlans = user.getCaloriePlanCollection().stream().map(modelMapperUtil::convertToDTO).
                    collect(Collectors.toList());
            List<MacronutrientFoodDTO> macronutrientFoodDTOS = user.getMacronutrientFoodCollection().stream().
                    map(modelMapperUtil::convertToDTO).collect(Collectors.toList());
            dto.setCaloriePlanList(caloriePlans);
            dto.setSuccess(true);
            dto.setAddress(modelMapperUtil.convertToDTO(user.getUserAddressCollection().get(0)));
            dto.setMacronutrientFoodList(macronutrientFoodDTOS);
            results.add(dto);
        });
        return results;
    }

    private void updateDetails(UserDTO userDTO, User user) {
        user.setHeight(userDTO.getHeight());
        user.setBodyFat(userDTO.getBodyFat());
        user.setFatFreeMass(userDTO.getFatFreeMass());
        user.setEstimatedBmr(userDTO.getEstimatedBmr());
        user.setActivityLevel(userDTO.getActivityLevel());
        user.setCaloriesToAdd(userDTO.getCaloriesToAdd());
        user.setDailyReq(userDTO.getDailyReq());
        user.setDailyReqNon(userDTO.getDailyReq());
        user.setCreated(new Date());
        userRepository.save(user);
    }

    private void updateMacronutrientFood(List<MacronutrientFoodDTO> list, User user) {

        list.forEach(dto -> {
            MacronutrientFood food = modelMapperUtil.convertToEntity(dto);
            food.setUserId(user);
            macronutrientFoodRepository.save(food);
        });
    }

    private void updateCaloriePlan(List<CaloriePlanDTO> list, User user) {

        list.forEach(dto -> {
            CaloriePlan caloriePlan = modelMapperUtil.convertToEntity(dto);
            caloriePlan.setUserId(user);
            caloriePlanRepository.save(caloriePlan);
        });
    }
}
