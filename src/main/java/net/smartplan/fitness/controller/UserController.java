package net.smartplan.fitness.controller;

import net.smartplan.fitness.dto.AddressDTO;
import net.smartplan.fitness.dto.IdentifyTraceDTO;
import net.smartplan.fitness.dto.UpdateUserStatusDTO;
import net.smartplan.fitness.dto.UserDTO;
import net.smartplan.fitness.response.CommonResponse;
import net.smartplan.fitness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.util.List;

/**
 * @author H.D. Sachin Dilshan
 */

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

    @GetMapping("/getAll")
    public List<UserDTO> getAll() {
        return userService.getAll();
    }

    @PostMapping("/daily/checkToDo")
    public IdentifyTraceDTO dailyCheckToDo(@RequestBody IdentifyTraceDTO identifyTraceDTO) throws ParseException {
        return userService.dailyCheckToDo(identifyTraceDTO);
    }

    @GetMapping("/checkDailyStatus/{email}")
    public IdentifyTraceDTO checkDailyCheckToDo(@PathVariable String email) {
        return userService.checkDailyStatus(email);
    }

    @GetMapping("/generateExcel/report")
    public ResponseEntity<InputStreamResource> excelGenerator() {

        ByteArrayInputStream in = userService.getUserReport();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=meals report.xlsx");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }
}
