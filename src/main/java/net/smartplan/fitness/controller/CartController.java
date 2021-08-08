package net.smartplan.fitness.controller;

import lombok.extern.slf4j.Slf4j;
import net.smartplan.fitness.dto.CartDTO;
import net.smartplan.fitness.dto.UserCatDetailsDTO;
import net.smartplan.fitness.response.CommonResponse;
import net.smartplan.fitness.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@CrossOrigin
@Slf4j
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody List<CartDTO> list) {

        try {
            cartService.save(list);
            return new ResponseEntity<>(new CommonResponse(true, "Success"), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(new CommonResponse(false, e.toString()), HttpStatus.OK);
        }
    }

    @PostMapping("/update")
    public boolean update(@RequestBody CartDTO cartDTO) {
        return cartService.update(cartDTO);
    }

    @GetMapping("/getByUser/{email}")
    public UserCatDetailsDTO getDetailsByUser(@PathVariable String email) {

        return cartService.getDetailsByUser(email);
    }
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable int id) {
        return cartService.delete(id);
    }
}
