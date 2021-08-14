package net.smartplan.fitness.controller;

import net.smartplan.fitness.dto.AddressDTO;
import net.smartplan.fitness.dto.UpdateUserStatusDTO;
import net.smartplan.fitness.dto.UserDTO;
import net.smartplan.fitness.response.CommonResponse;
import net.smartplan.fitness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }

    @GetMapping("/checkEmail/{email}")
    public boolean checkEmail(@PathVariable String email) {
        return userService.checkEmail(email);
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }

    @PostMapping("/updateAddress")
    public AddressDTO updateAddress(@RequestBody AddressDTO dto) {
        return userService.updateAddress(dto);
    }

    @PostMapping("/updateUserStatus")
    public UpdateUserStatusDTO updateUserStatus(@RequestBody UpdateUserStatusDTO dto) {
        return userService.updateUserStatus(dto);
    }

    @PostMapping("/update")
    public ResponseEntity<CommonResponse> updateUser(@RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO);
        return new ResponseEntity<>(new CommonResponse(true, "Success"), HttpStatus.OK);
    }
    @PostMapping("/updateV1")
    public ResponseEntity<CommonResponse> recalculate(@RequestBody UserDTO userDTO) {
        userService.recalculate(userDTO);
        return new ResponseEntity<>(new CommonResponse(true, "Success"), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public List<UserDTO> getAll() {
        return userService.getAll();
    }


}
