package net.smartplan.fitness.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.smartplan.fitness.dto.*;
import net.smartplan.fitness.entity.*;
import net.smartplan.fitness.repository.*;
import net.smartplan.fitness.service.UserService;
import net.smartplan.fitness.util.ModelMapperUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.spec.KeySpec;
import java.text.DateFormat;
import java.text.ParseException;
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
    private final AdminDetailsRepository adminDetailsRepository;

    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DEEDED_ENCRYPTION_SCHEME = "DESede";
    public static final String ACTIVE = "ACTIVE";
    public static final String DISABLED = "DISABLED";
    public static final String EXPIRED = "EXPIRED";
    private Cipher cipher;
    byte[] arrayBytes;
    SecretKey key;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           CaloriePlanRepository caloriePlanRepository,
                           MacronutrientFoodRepository macronutrientFoodRepository,
                           ModelMapperUtil modelMapperUtil,
                           AddressRepository addressRepository, IdentifyTraceRepository identifyTraceRepository, AdminDetailsRepository adminDetailsRepository) {
        this.userRepository = userRepository;
        this.caloriePlanRepository = caloriePlanRepository;
        this.macronutrientFoodRepository = macronutrientFoodRepository;
        this.modelMapperUtil = modelMapperUtil;
        this.addressRepository = addressRepository;
        this.identifyTraceRepository = identifyTraceRepository;
        this.adminDetailsRepository = adminDetailsRepository;

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
            user.setUserRole("USER");
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

            DateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            Date parse1 = formatter.parse(date1);
            Date parse2 = formatter.parse(date2);

            long diff = Math.abs(parse2.getTime() - parse1.getTime());

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

                    UserDTO dto = new UserDTO();
                    dto.setExpiredGoalDate(user.getExpiredGoalDate());
                    dto.setName(user.getName());
                    dto.setId(user.getId());
                    dto.setGender(user.getGender());
                    dto.setConsulter(user.getConsulter());
                    dto.setEmail(user.getEmail());
                    dto.setAge(user.getAge());
                    dto.setDailyReq(user.getDailyReq());
                    dto.setDailyReqNon(user.getDailyReqNon());
                    dto.setConsulter(user.getConsulter());
                    dto.setActivityLevel(user.getActivityLevel());
                    dto.setCreated(user.getCreated());
                    dto.setHeight(user.getHeight());
                    dto.setWeight(user.getWeight());
                    dto.setUserRole(user.getUserRole());
                    dto.setGoalType(user.getGoalType());
                    dto.setEstimatedBmr(user.getEstimatedBmr());
                    dto.setStatus(user.getStatus());

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
            userAddress.setAddress(addressDTO.getAddress());
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

                DateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                Date parse1 = formatter.parse(date1);
                Date parse2 = formatter.parse(date2);

                long diff = Math.abs(parse2.getTime() - parse1.getTime());

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
    public List<UserDTO> getAll() {

        List<User> all = userRepository.findAll();
        List<UserDTO> results = new ArrayList<>();
        all.forEach(user -> {
            UserDTO dto = new UserDTO();
            dto.setExpiredGoalDate(user.getExpiredGoalDate());
            dto.setName(user.getName());
            dto.setGender(user.getGender());
            dto.setConsulter(user.getConsulter());
            dto.setEmail(user.getEmail());
            dto.setAge(user.getAge());
            dto.setHeight(user.getHeight());
            dto.setWeight(user.getWeight());
            dto.setStatus(user.getStatus());
            dto.setFatFreeMass(user.getFatFreeMass());
            dto.setEstimatedBmr(user.getEstimatedBmr());
            dto.setActivityLevel(user.getActivityLevel());
            dto.setBodyFat(user.getBodyFat());
            dto.setCreated(user.getCreated());
            dto.setId(user.getId());

            List<CaloriePlanDTO> caloriePlans = user.getCaloriePlanCollection().stream().map(modelMapperUtil::convertToDTO).
                    collect(Collectors.toList());
            List<MacronutrientFoodDTO> macronutrientFoodDTOS = user.getMacronutrientFoodCollection().stream().
                    map(modelMapperUtil::convertToDTO).collect(Collectors.toList());
            dto.setCaloriePlanList(caloriePlans);
            dto.setSuccess(true);
            dto.setAddress(modelMapperUtil.convertToDTO(user.getUserAddressCollection().get(0)));
            dto.setMacronutrientFoodList(macronutrientFoodDTOS);
            dto.setPendingGoalDays(this.checkDailyStatus(user.getEmail()).getGoalDays());
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
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
                String date1 = sdf.format(new Date());
                String date2 = sdf.format(trace.getUpdated());

                log.info("UserServiceImpl :: IdentifyTraceDTO :: dailyCheckToDo :: CURRENT DATE" + date1);
                log.info("UserServiceImpl :: IdentifyTraceDTO :: dailyCheckToDo :: UPDATED DATE" + date2);

                if (!date1.equals(date2)) {
                    double tempDays = trace.getGoalDays() - 1;

                    if (tempDays > 0) {
                        dto.setStatus(ACTIVE);
                        dto.setGoalDays(trace.getGoalDays() - 1);
                    } else {
                        dto.setStatus(EXPIRED);
                        dto.setGoalDays((double) 0);
                    }

                    dto.setId(trace.getId());
                    dto.setDailyStatus(false);
                    dto.setClickedToDo(true);
                    dto.setUpdated(new Date());
                    dto.setEmail(trace.getEmail());
                    dto.setCreated(trace.getCreated());
                    dto.setGoalExpiredDate(trace.getGoalExpiredDate());
                    dto.setStatus(ACTIVE);

                    identifyTraceRepository.save(modelMapperUtil.convertToEntity(dto));
                } else {
                    dto.setStatus(DISABLED);
                }
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
                SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

                dto.setEmail(trace.getEmail());
                dto.setCreated(trace.getCreated());
                dto.setGoalExpiredDate(trace.getGoalExpiredDate());
                dto.setId(trace.getId());
                dto.setClickedToDo(trace.getClickedToDo());
                dto.setDailyStatus(fmt.format(new Date()).equals(fmt.format((trace.getUpdated()))));
                dto.setStatus(trace.getStatus());
                dto.setGoalDays(trace.getGoalDays());
                dto.setUpdated(trace.getUpdated());

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

    @Override
    public ByteArrayInputStream getUserReport() {
        String[] columns = {"Id", "Name", "Gender", "Email", "Age", "Height", "Weight", "Consulter", "Fat Free Mass", "Activity Level", "Body Fat", "Estimated Bmr"
                , "Address", "Created", "Status"};

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("User");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        // Row for Header
        Row headerRow = sheet.createRow(0);
        // Header
        for (int col = 0; col < columns.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(columns[col]);
            cell.setCellStyle(headerCellStyle);
        }
        // CellStyle for Age
        CellStyle ageCellStyle = workbook.createCellStyle();
        ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));
        List<UserDTO> list = getAll();
        int rowIdx = 1;
        for (UserDTO dto : list) {
            Row row = sheet.createRow(rowIdx++);
            DateFormat formatter = new SimpleDateFormat("dd-M-yyyy");

            row.createCell(0).setCellValue(dto.getId());
            row.createCell(1).setCellValue(dto.getName());
            row.createCell(2).setCellValue(dto.getGender());
            row.createCell(3).setCellValue(dto.getEmail());
            row.createCell(4).setCellValue(dto.getAge());
            row.createCell(5).setCellValue(dto.getHeight());
            row.createCell(6).setCellValue(dto.getWeight());
            row.createCell(7).setCellValue(dto.getConsulter());
            row.createCell(8).setCellValue(dto.getFatFreeMass());
            row.createCell(9).setCellValue(dto.getActivityLevel());
            row.createCell(10).setCellValue(dto.getBodyFat());
            row.createCell(11).setCellValue(dto.getEstimatedBmr());
            row.createCell(12).setCellValue(dto.getAddress().getAddress());
            row.createCell(13).setCellValue(formatter.format(dto.getCreated()));
            row.createCell(14).setCellValue(dto.getStatus());

        }

        try {
            workbook.write(out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public AdminDTO adminLogin(AdminDTO adminDTO) {
        adminDTO.setCreated(new Date());
        AdminDetails adminDetails = adminDetailsRepository.findByEmail(adminDTO.getEmail());

        if (decryptPassword(adminDetails.getPassword()).equals(adminDTO.getPassword())) {
            log.info("ADMIN PASSWORD IS CORRECT!!");
            adminDTO.setLogged(true);
        }

        return adminDTO;
    }

}
