package net.smartplan.fitness.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.smartplan.fitness.dto.*;
import net.smartplan.fitness.entity.Meal;
import net.smartplan.fitness.entity.Purchase;
import net.smartplan.fitness.entity.PurchaseDetails;
import net.smartplan.fitness.repository.MealRepository;
import net.smartplan.fitness.repository.PurchaseDetailsRepository;
import net.smartplan.fitness.repository.PurchaseRepository;
import net.smartplan.fitness.service.EmailService;
import net.smartplan.fitness.service.PurchaseService;
import net.smartplan.fitness.util.ModelMapperUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseDetailsRepository detailsRepository;
    private final ModelMapperUtil mapperUtil;
    private final EmailService emailService;
    private final MealRepository mealRepository;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository,
                               PurchaseDetailsRepository detailsRepository,
                               ModelMapperUtil mapperUtil, EmailService emailService,
                               MealRepository mealRepository) {
        this.purchaseRepository = purchaseRepository;
        this.detailsRepository = detailsRepository;
        this.mapperUtil = mapperUtil;
        this.emailService = emailService;
        this.mealRepository = mealRepository;
    }

    @Override
    public PurchaseDTO save(PurchaseDTO purchaseDTO) {

        //for generate purchase id
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.toUpperCase(Locale.ENGLISH);
        uuid = uuid.replace("-", "");
        String uuid10digits = uuid.substring(uuid.length() - 10);

        purchaseDTO.setPurchaseId(uuid10digits);

        Purchase purchase = purchaseRepository.save(mapperUtil.convertToEntity(purchaseDTO));
        List<EmailBodyDTO> bodyDTOS = new ArrayList<>();
        final double[] totalPrice = {0};
        final String[] address = {""};
        purchaseDTO.getPurchaseDetails().forEach(detail -> {
            PurchaseDetails purchaseDetails = mapperUtil.convertToEntity(detail);
            purchaseDetails.setPurchaseId(purchase);
            purchaseDetails.setStatus(detail.getStatus());
            detailsRepository.save(purchaseDetails);
            Optional<Meal> meal = mealRepository.findById(detail.getMealId().getId());
            String name = "";
            address[0] = detail.getShippingAddress();
            if (meal.isPresent()) {
                name = meal.get().getMealName();
            }
            totalPrice[0] += purchaseDetails.getPrice();
            bodyDTOS.add(createDTO(name, detail.getQuantity(), detail.getPrice()));

        });
        emailService.sendEmailWithTemplate(bodyDTOS, purchase.getEmail(), totalPrice[0], address[0]);
        return mapperUtil.convertToDTO(purchase);
    }

    @Override
    public List<PurchaseDTO> getAllByUser(String email) {

        List<PurchaseDTO> list = new ArrayList<>();
        List<Purchase> purchases = purchaseRepository.findAllByEmailOrderByCreatedDesc(email);
        return getPurchaseDTOS(list, purchases);
    }

    @Override
    public List<PurchaseDTO> fetchAllOrders() {
        List<PurchaseDTO> list = new ArrayList<>();
        Iterable<Purchase> all = purchaseRepository.findAll();
        return getPurchaseDTOS(list, all);
    }

    @Override
    public void updateOrderStatus(UpdatedPurchaseDetailsDTO updatedPurchaseDetailsDTO) {
        Optional<PurchaseDetails> purchaseDetails = detailsRepository.findById(updatedPurchaseDetailsDTO.getId());

        if (purchaseDetails.isPresent()) {
            PurchaseDetails details = purchaseDetails.get();
            details.setId(details.getId());
            details.setStatus(updatedPurchaseDetailsDTO.getStatus());
            detailsRepository.save(details);
            mapperUtil.convertToDTO(details);
            return;
        }
        new PurchaseDetailsDTO();
    }

    private List<PurchaseDTO> getPurchaseDTOS(List<PurchaseDTO> list, Iterable<Purchase> all) {
        all.forEach(purchase -> {
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
        dto.setAmount("$. ".concat(Double.toString(amount)));
        return dto;
    }

    public List<PurchaseDetailsDTO> fetchAllPurchaseDetails() {
        List<PurchaseDetailsDTO> list = new ArrayList<>();
        Iterable<PurchaseDetails> all = detailsRepository.findAll();
        all.forEach(details -> {

            PurchaseDetailsDTO purchaseDetailsDTO = new PurchaseDetailsDTO();

            purchaseDetailsDTO.setPurchaseDetailsId(details.getPurchaseId().getPurchaseId());
            purchaseDetailsDTO.setMealName(details.getMealId().getMealName());
            purchaseDetailsDTO.setComment(details.getComment());
            purchaseDetailsDTO.setQuantity(details.getQuantity());
            purchaseDetailsDTO.setPrice(details.getPrice());
            purchaseDetailsDTO.setOrderDate(details.getOrderDate());
            purchaseDetailsDTO.setOrderTime(details.getOrderTime());
            purchaseDetailsDTO.setShippingAddress(details.getShippingAddress());
            purchaseDetailsDTO.setStatus(details.getStatus());
            list.add(purchaseDetailsDTO);
        });
        return list;
    }

    @Override
    public ByteArrayInputStream getOrdersReport() {
        String[] columns = {"Order Id", "Meal Name", "Quantity", "Price($)", "Shipping Address", "Comment", "Order Date", "Status"};

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("Order");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        // Row for Header
        Row headerRow = sheet.createRow(0);
        // Header
        for (int col = 0; col < columns.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(columns[col]);
            cell.setCellStyle(headerCellStyle);
        }
        // CellStyle for Age
        CellStyle ageCellStyle = workbook.createCellStyle();
        ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));
        List<PurchaseDetailsDTO> list = fetchAllPurchaseDetails();
        int rowIdx = 1;
        for (PurchaseDetailsDTO dto : list) {
            Row row = sheet.createRow(rowIdx++);

            row.createCell(0).setCellValue(dto.getPurchaseDetailsId());
            row.createCell(1).setCellValue(dto.getMealName());
            row.createCell(2).setCellValue(dto.getQuantity());
            row.createCell(3).setCellValue(dto.getPrice());
            row.createCell(4).setCellValue(dto.getShippingAddress());
            row.createCell(5).setCellValue(dto.getComment());
            row.createCell(6).setCellValue(dto.getOrderDate());
            row.createCell(7).setCellValue(dto.getStatus());

        }

        try {
            workbook.write(out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return new ByteArrayInputStream(out.toByteArray());
    }


}
