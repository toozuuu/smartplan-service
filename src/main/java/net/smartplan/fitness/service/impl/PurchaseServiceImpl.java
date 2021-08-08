package net.smartplan.fitness.service.impl;

import net.smartplan.fitness.dto.EmailBodyDTO;
import net.smartplan.fitness.dto.PurchaseDTO;
import net.smartplan.fitness.dto.PurchaseDetailsDTO;
import net.smartplan.fitness.entity.Meal;
import net.smartplan.fitness.entity.Purchase;
import net.smartplan.fitness.entity.PurchaseDetails;
import net.smartplan.fitness.repository.MealRepository;
import net.smartplan.fitness.repository.PurchaseDetailsRepository;
import net.smartplan.fitness.repository.PurchaseRepository;
import net.smartplan.fitness.service.CartService;
import net.smartplan.fitness.service.EmailService;
import net.smartplan.fitness.service.PurchaseService;
import net.smartplan.fitness.util.ModelMapperUtil;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = Exception.class)
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseDetailsRepository detailsRepository;
    private final ModelMapperUtil mapperUtil;
    private final EmailService emailService;
    private final MealRepository mealRepository;
    private final CartService cartService;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository,
                               PurchaseDetailsRepository detailsRepository,
                               ModelMapperUtil mapperUtil, EmailService emailService,
                               MealRepository mealRepository, CartService cartService) {
        this.purchaseRepository = purchaseRepository;
        this.detailsRepository = detailsRepository;
        this.mapperUtil = mapperUtil;
        this.emailService = emailService;
        this.mealRepository = mealRepository;
        this.cartService = cartService;
    }

    @Override
    public PurchaseDTO save(PurchaseDTO purchaseDTO) {

        Purchase purchase = purchaseRepository.save(mapperUtil.convertToEntity(purchaseDTO));
        List<EmailBodyDTO> bodyDTOS = new ArrayList<>();
        final double[] totalPrice = {0};
        final String[] address = {""};
        purchaseDTO.getPurchaseDetails().forEach(detail -> {
            PurchaseDetails purchaseDetails = mapperUtil.convertToEntity(detail);
            purchaseDetails.setPurchaseId(purchase);
           // cartService.deleteMealAndUserName(detail.getMealId().getId(),purchaseDTO.getEmail());
            detailsRepository.save(purchaseDetails);
            Optional<Meal> meal = mealRepository.findById(detail.getMealId().getId());
            String name = "";
            address[0] = detail.getShippingAddress();
            if(meal.isPresent()) {
                 name = meal.get().getMealName();
            }
            totalPrice[0] += purchaseDetails.getPrice();
            bodyDTOS.add(createDTO(name,detail.getQuantity(),detail.getPrice()));

        });
        emailService.sendEmailWithTemplate(bodyDTOS,purchase.getEmail(),totalPrice[0],address[0]);
        return mapperUtil.convertToDTO(purchase);
    }

    @Override
    public List<PurchaseDTO> getAllByUser(String email) {

        List<PurchaseDTO> list = new ArrayList<>();
        List<Purchase> purchases = purchaseRepository.findAllByEmail(email);
        purchases.forEach(purchase -> {
            PurchaseDTO purchaseDTO = mapperUtil.convertToDTO(purchase);
            List<PurchaseDetailsDTO> purchaseDetailsDTOS = purchase.getPurchaseDetailsCollection().stream().
                    map(mapperUtil::convertToDTO).collect(Collectors.toList());
            purchaseDTO.setPurchaseDetails(purchaseDetailsDTOS);
            list.add(purchaseDTO);
        });
        return list;
    }

    private EmailBodyDTO createDTO(String name, double qty, double amount) {

        EmailBodyDTO dto = new EmailBodyDTO();
        dto.setName(name);
        dto.setQuantity(Double.toString(qty));
        dto.setAmount("Rs. ".concat(Double.toString(amount)));
        return dto;
    }
}
