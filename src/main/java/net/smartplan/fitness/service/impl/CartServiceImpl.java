package net.smartplan.fitness.service.impl;

import net.smartplan.fitness.dto.CartDTO;
import net.smartplan.fitness.dto.UserCatDetailsDTO;
import net.smartplan.fitness.entity.Cart;
import net.smartplan.fitness.repository.CartRepository;
import net.smartplan.fitness.service.CartService;
import net.smartplan.fitness.util.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final ModelMapperUtil modelMapperUtil;
    private final CartRepository cartRepository;

    @Autowired
    public CartServiceImpl(ModelMapperUtil modelMapperUtil, CartRepository cartRepository) {
        this.modelMapperUtil = modelMapperUtil;
        this.cartRepository = cartRepository;
    }

    @Override
    public boolean save(List<CartDTO> list) {
        cartRepository.saveAll(list.stream().map(modelMapperUtil::convertToEntity).collect(Collectors.toList()));
        return true;
    }

    @Override
    public boolean update(CartDTO cartDTO) {

        Optional<Cart> optionalCart = cartRepository.findById(cartDTO.getId());

        if (optionalCart.isPresent()) {

            Cart cart = optionalCart.get();
            cart.setQuantity(cartDTO.getQuantity());
            cartRepository.save(cart);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public UserCatDetailsDTO getDetailsByUser(String user) {

        List<Cart> carts = cartRepository.findAllByEmail(user);
        UserCatDetailsDTO dto = new UserCatDetailsDTO();
        List<CartDTO> cartDTOS = new ArrayList<>();
        final double[] totalPrice = {0};

        carts.forEach(detail -> {
            totalPrice[0] += detail.getUnitPrice()*detail.getQuantity();
            cartDTOS.add(modelMapperUtil.convertToDTO(detail));
        });
        dto.setTotalPrice(totalPrice[0]);
        dto.setCartDTOList(cartDTOS);
        return dto;
    }

    @Override
    public boolean delete(int id) {
        Optional<Cart> optionalCart = cartRepository.findById(id);
        if (optionalCart.isPresent()) {
            cartRepository.delete(optionalCart.get());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteMealAndUserName(int mealId, String userName) {

        Cart cart = cartRepository.findByEmailAndMealId_Id(userName, mealId);
        if (cart != null) {
            cartRepository.delete(cart);
        }
    }
}
