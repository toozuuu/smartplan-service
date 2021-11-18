package net.smartplan.fitness.service;

import net.smartplan.fitness.dto.*;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.util.List;


/**
 * @author H.D. Sachin Dilshan
 */

public interface UserService {

    UserDTO register(UserDTO userDTO);

    boolean checkEmail(String email);

    UserDTO login(UserDTO userDTO);

    AddressDTO updateAddress(AddressDTO addressDTO);

    UpdateUserStatusDTO updateUserStatus(UpdateUserStatusDTO updateUserStatusDTO);

    void updateUser(UserDTO userDTO);

    List<UserDTO> getAll();

    IdentifyTraceDTO dailyCheckToDo(IdentifyTraceDTO identifyTraceDTO) throws ParseException;

    IdentifyTraceDTO checkDailyStatus(String email);

    ByteArrayInputStream getUserReport();

    AdminDTO adminLogin(AdminDTO adminDTO);
}
