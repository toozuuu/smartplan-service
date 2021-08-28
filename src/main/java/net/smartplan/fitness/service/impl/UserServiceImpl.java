package net.smartplan.fitness.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.smartplan.fitness.dto.*;
import net.smartplan.fitness.entity.*;
import net.smartplan.fitness.repository.*;
import net.smartplan.fitness.service.UserService;
import net.smartplan.fitness.util.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.transaction.Transactional;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CaloriePlanRepository caloriePlanRepository;
    private final MacronutrientFoodRepository macronutrientFoodRepository;
    private final ModelMapperUtil modelMapperUtil;
    private final AddressRepository addressRepository;
    private final IdentifyTraceRepository identifyTraceRepository;

    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DEEDED_ENCRYPTION_SCHEME = "DESede";
    public static final String ACTIVE = "ACTIVE";
    public static final String EXPIRED = "EXPIRED";
    private Cipher cipher;
    byte[] arrayBytes;
    SecretKey key;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           CaloriePlanRepository caloriePlanRepository,
                           MacronutrientFoodRepository macronutrientFoodRepository,
                           ModelMapperUtil modelMapperUtil,
                           AddressRepository addressRepository, IdentifyTraceRepository identifyTraceRepository) {
        this.userRepository = userRepository;
        this.caloriePlanRepository = caloriePlanRepository;
        this.macronutrientFoodRepository = macronutrientFoodRepository;
        this.modelMapperUtil = modelMapperUtil;
        this.addressRepository = addressRepository;
        this.identifyTraceRepository = identifyTraceRepository;

        try {
            String myEncryptionKey = "H.D.SACHIN DILSHAN NANDANA";
            arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
            KeySpec ks = new DESedeKeySpec(arrayBytes);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(DEEDED_ENCRYPTION_SCHEME);
            cipher = Cipher.getInstance(DEEDED_ENCRYPTION_SCHEME);
            key = skf.generateSecret(ks);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public UserDTO register(UserDTO userDTO) {
        try {
            userDTO.setPassword(encryptPassword(userDTO.getPassword()));

            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

            calendar.add(Calendar.MONTH, (int) Math.round(userDTO.getGoalTime()));

            String format = sdf1.format(calendar.getTime());

            userDTO.setExpiredGoalDate(sdf1.parse(format));

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

            IdentifyTraceDTO identifyTraceDTO = new IdentifyTraceDTO();
            identifyTraceDTO.setEmail(userDTO.getEmail());
            identifyTraceDTO.setDailyStatus(true);
            identifyTraceDTO.setGoalExpiredDate(sdf1.parse(format));

            Date firstDate = sdf.parse(new Date().toString());
            Date secondDate = sdf.parse(calendar.getTime().toString());

            String date1 = sdf1.format(firstDate);
            String date2 = sdf1.format(secondDate);

            long diff = Math.abs(new Date(date2).getTime() - new Date(date1).getTime());

            identifyTraceDTO.setGoalDays((double) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

            identifyTraceRepository.save(modelMapperUtil.convertToEntity(identifyTraceDTO));

            return dto;
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return new UserDTO();

    }


    @Override
    public boolean checkEmail(String email) {

        User user = userRepository.findByEmail(email);
        return user != null;
    }

    @Override
    public UserDTO login(UserDTO userDTO) {
        try {
            userDTO.setCreated(new Date());
            User user = userRepository.findByEmail(userDTO.getEmail());
            if (decryptPassword(user.getPassword()).equals(userDTO.getPassword())) {
                if (user.getStatus().equals(ACTIVE)) {
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

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return new UserDTO();

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

            List<IdentifyTrace> activeTraces = identifyTraceRepository.findAllByEmailAndStatus(user.getEmail(), ACTIVE);

            if (!activeTraces.isEmpty()) {
                for (IdentifyTrace trace : activeTraces) {

                    IdentifyTraceDTO identifyTraceDTO = new IdentifyTraceDTO();
                    identifyTraceDTO.setId(trace.getId());
                    identifyTraceDTO.setStatus(EXPIRED);
                    identifyTraceDTO.setUpdated(new Date());

                    identifyTraceRepository.save(modelMapperUtil.convertToEntity(identifyTraceDTO));

                }
            }


            try {

                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

                calendar.add(Calendar.MONTH, (int) Math.round(userDTO.getGoalTime()));

                String format = sdf1.format(calendar.getTime());

                userDTO.setExpiredGoalDate(sdf1.parse(format));

                String date1 = sdf1.format(sdf.parse(new Date().toString()));
                String date2 = sdf1.format(sdf.parse(calendar.getTime().toString()));

                long diff = Math.abs(new Date(date2).getTime() - new Date(date1).getTime());

                IdentifyTraceDTO identifyTraceDTO = new IdentifyTraceDTO();

                identifyTraceDTO.setEmail(user.getEmail());
                identifyTraceDTO.setDailyStatus(true);
                identifyTraceDTO.setGoalExpiredDate(sdf1.parse(format));
                identifyTraceDTO.setGoalDays((double) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

                identifyTraceRepository.save(modelMapperUtil.convertToEntity(identifyTraceDTO));

            } catch (Exception exception) {
                exception.printStackTrace();
            }

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

    @Override
    public IdentifyTraceDTO dailyCheckToDo(IdentifyTraceDTO identifyTraceDTO) {
        List<IdentifyTrace> identifyTrace = identifyTraceRepository.findAllByEmailAndStatus(identifyTraceDTO.getEmail(), ACTIVE);

        if (!identifyTrace.isEmpty()) {
            IdentifyTraceDTO dto = new IdentifyTraceDTO();

            for (IdentifyTrace trace : identifyTrace) {
                double tempDays = trace.getGoalDays() - 1;
                SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

                if (fmt.format(new Date()).equals(fmt.format((trace.getUpdated())))) {
                    dto.setDailyStatus(false);

                    if (tempDays > 0) {
                        dto.setStatus(ACTIVE);
                        dto.setGoalDays(trace.getGoalDays() - 1);
                    } else {
                        dto.setStatus(EXPIRED);
                        dto.setGoalDays((double) 0);
                    }
                    dto.setUpdated(new Date());

                } else {
                    dto.setGoalDays(trace.getGoalDays());
                    dto.setUpdated(trace.getUpdated());
                    dto.setDailyStatus(true);
                }

                dto.setEmail(trace.getEmail());
                dto.setCreated(trace.getCreated());
                dto.setGoalExpiredDate(trace.getGoalExpiredDate());
                dto.setId(trace.getId());

                identifyTraceRepository.save(modelMapperUtil.convertToEntity(dto));

            }

            return dto;
        }
        return new IdentifyTraceDTO();
    }

    @Override
    public IdentifyTraceDTO checkDailyStatus(String email) {
        List<IdentifyTrace> identifyTrace = identifyTraceRepository.findAllByEmailAndStatus(email, ACTIVE);

        if (!identifyTrace.isEmpty()) {
            IdentifyTraceDTO dto = new IdentifyTraceDTO();

            for (IdentifyTrace trace : identifyTrace) {

                try {
                    dto.setEmail(trace.getEmail());
                    dto.setCreated(trace.getCreated());
                    dto.setGoalExpiredDate(trace.getGoalExpiredDate());
                    dto.setId(trace.getId());
                    dto.setDailyStatus(trace.getDailyStatus());
                    dto.setStatus(ACTIVE);
                    dto.setGoalDays(trace.getGoalDays());
                    dto.setUpdated(new Date());

                    identifyTraceRepository.save(modelMapperUtil.convertToEntity(dto));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }


            }
            return dto;
        }

        return new IdentifyTraceDTO();
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

    public String encryptPassword(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = Base64.getEncoder().encodeToString(encryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }


    public String decryptPassword(String encryptedString) {
        String decryptedText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.getDecoder().decode(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText = new String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

}
