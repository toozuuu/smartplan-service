package net.smartplan.fitness.service;

import net.smartplan.fitness.dto.AddressDTO;
import net.smartplan.fitness.dto.IdentifyTraceDTO;
import net.smartplan.fitness.dto.UpdateUserStatusDTO;
import net.smartplan.fitness.dto.UserDTO;

import java.io.ByteArrayInputStream;
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

    IdentifyTraceDTO dailyCheckToDo(IdentifyTraceDTO identifyTraceDTO);

    IdentifyTraceDTO checkDailyStatus(String email);

    ByteArrayInputStream getUserReport();

}
