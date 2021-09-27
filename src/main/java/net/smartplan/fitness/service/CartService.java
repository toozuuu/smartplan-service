package net.smartplan.fitness.service;

import net.smartplan.fitness.dto.CartDTO;
import net.smartplan.fitness.dto.UserCatDetailsDTO;

import java.util.List;


/**
 * @author H.D. Sachin Dilshan
 */


public interface CartService {

    boolean save(List<CartDTO> list);

    boolean update(CartDTO cartDTO);

    UserCatDetailsDTO getDetailsByUser(String user);

    boolean delete(int id);

}
