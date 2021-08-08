package net.smartplan.fitness.service;

import net.smartplan.fitness.dto.CartDTO;
import net.smartplan.fitness.dto.UserCatDetailsDTO;

import java.util.List;

public interface CartService {

    boolean save(List<CartDTO> list);

    boolean update(CartDTO cartDTO);

    UserCatDetailsDTO getDetailsByUser(String user);

    boolean delete(int id);

    void deleteMealAndUserName(int mealId, String userName);
}
