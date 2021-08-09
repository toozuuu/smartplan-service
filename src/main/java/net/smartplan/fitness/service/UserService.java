package net.smartplan.fitness.service;

import net.smartplan.fitness.dto.AddressDTO;
import net.smartplan.fitness.dto.UpdateUserStatusDTO;
import net.smartplan.fitness.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO register(UserDTO userDTO);

    boolean checkEmail(String email);

    UserDTO login(UserDTO userDTO);

    AddressDTO updateAddress(AddressDTO addressDTO);

    UpdateUserStatusDTO updateUserStatus(UpdateUserStatusDTO updateUserStatusDTO);

    UserDTO updateUser(UserDTO userDTO);

    UserDTO recalculate(UserDTO userDTO);

    List<UserDTO> getAll();
}
