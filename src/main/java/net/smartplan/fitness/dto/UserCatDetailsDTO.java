package net.smartplan.fitness.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserCatDetailsDTO {

    private double totalPrice;
    private List<CartDTO> cartDTOList;
}
