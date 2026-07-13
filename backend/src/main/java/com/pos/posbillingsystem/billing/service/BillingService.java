package com.pos.posbillingsystem.billing.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.pos.posbillingsystem.billing.dto.BillItemResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.pos.posbillingsystem.inventory.entity.InventoryLog;
import com.pos.posbillingsystem.inventory.enums.InventoryAction;
import com.pos.posbillingsystem.billing.repository.BillItemRepository;
import com.pos.posbillingsystem.billing.repository.BillRepository;
import com.pos.posbillingsystem.customer.repository.CustomerRepository;
import com.pos.posbillingsystem.inventory.repository.InventoryLogRepository;
import com.pos.posbillingsystem.product.repository.ProductRepository;
import com.pos.posbillingsystem.user.repository.UserRepository;
import com.pos.posbillingsystem.billing.dto.BillRequest;
import com.pos.posbillingsystem.billing.dto.BillResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.pos.posbillingsystem.billing.dto.BillItemRequest;
import com.pos.posbillingsystem.billing.entity.Bill;
import com.pos.posbillingsystem.billing.entity.BillItem;
import com.pos.posbillingsystem.billing.enums.PaymentStatus;
import com.pos.posbillingsystem.customer.entity.Customer;
import com.pos.posbillingsystem.product.entity.Product;
import com.pos.posbillingsystem.user.entity.User;
@Service
public class BillingService {

    private final BillRepository billRepository;
    private final BillItemRepository billItemRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final InventoryLogRepository inventoryLogRepository;

    public BillingService(
            BillRepository billRepository,
            BillItemRepository billItemRepository,
            ProductRepository productRepository,
            CustomerRepository customerRepository,
            UserRepository userRepository,
            InventoryLogRepository inventoryLogRepository) {

        this.billRepository = billRepository;
        this.billItemRepository = billItemRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.inventoryLogRepository = inventoryLogRepository;
    }
    @Transactional
    public BillResponse createBill(BillRequest request) {

        Customer customer = validateCustomer(request.getCustomerId());

        User user = validateUser(request.getUserId());

        String invoiceNumber = generateInvoiceNumber();

        Bill bill = new Bill();

        bill.setInvoiceNumber(invoiceNumber);
        bill.setCustomerId(customer != null ? customer.getCustomerId() : null);
        bill.setUserId(user.getUserId());

        bill.setSubtotal(BigDecimal.ZERO);
        bill.setDiscount(BigDecimal.ZERO);
        bill.setGstAmount(BigDecimal.ZERO);
        bill.setTotalAmount(BigDecimal.ZERO);

        bill.setPaymentMethod(request.getPaymentMethod());
        bill.setPaymentStatus(PaymentStatus.PAID);

        bill = billRepository.save(bill);
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        BigDecimal totalGST = BigDecimal.ZERO;

        for (BillItemRequest itemRequest : request.getItems()) {

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeException(product.getProductName() + " has insufficient stock");
            }

            BigDecimal quantity = BigDecimal.valueOf(itemRequest.getQuantity());

            BigDecimal unitPrice = product.getSellingPrice();

            BigDecimal lineSubtotal = unitPrice.multiply(quantity);

            BigDecimal discount = itemRequest.getDiscount() == null
                    ? BigDecimal.ZERO
                    : itemRequest.getDiscount();

            BigDecimal taxableAmount = lineSubtotal.subtract(discount);

            BigDecimal gst = taxableAmount
                    .multiply(product.getGstPercentage())
                    .divide(BigDecimal.valueOf(100));

            BigDecimal lineTotal = taxableAmount.add(gst);

            // Update running totals
            subtotal = subtotal.add(lineSubtotal);
            totalDiscount = totalDiscount.add(discount);
            totalGST = totalGST.add(gst);

            // Save BillItem
            BillItem billItem = new BillItem();
            billItem.setBillId(bill.getBillId());
            billItem.setProductId(product.getProductId());
            billItem.setProductName(product.getProductName());
            billItem.setQuantity(itemRequest.getQuantity());
            billItem.setUnitPrice(unitPrice);
            billItem.setDiscount(discount);
            billItem.setGstPercentage(product.getGstPercentage());
            billItem.setTotalPrice(lineTotal);
            System.out.println("Product Name = " + product.getProductName());
            System.out.println("BillItem Product Name = " + billItem.getProductName());

            billItemRepository.save(billItem);

            // Reduce stock
            product.setStockQuantity(
                    product.getStockQuantity() - itemRequest.getQuantity());

            productRepository.save(product);

            // Create Inventory Log
            InventoryLog log = new InventoryLog();
            log.setProductId(product.getProductId());
            log.setQuantityChanged(-itemRequest.getQuantity());
            log.setAction(InventoryAction.SALE);
            log.setRemarks("Invoice : " + bill.getInvoiceNumber());

            inventoryLogRepository.save(log);
        }
        // update bill totals
        BigDecimal finalTotal = subtotal
                .subtract(totalDiscount)
                .add(totalGST);

        bill.setSubtotal(subtotal);
        bill.setDiscount(totalDiscount);
        bill.setGstAmount(totalGST);
        bill.setTotalAmount(finalTotal);

        billRepository.save(bill);

        System.out.println("Bill Created : " + bill.getBillId());

        return mapToResponse(bill);
    }
    public BillResponse getBillById(Integer billId) {

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        BillResponse response = mapToResponse(bill);

        List<BillItem> billItems = billItemRepository.findByBillId(billId);

        List<BillItemResponse> itemResponses = new ArrayList<>();

        for (BillItem item : billItems) {

            BillItemResponse dto = new BillItemResponse();

            dto.setProductId(item.getProductId());
            dto.setProductName(item.getProductName());
            dto.setQuantity(item.getQuantity());
            dto.setUnitPrice(item.getUnitPrice());
            dto.setDiscount(item.getDiscount());
            dto.setGstPercentage(item.getGstPercentage());
            dto.setTotalPrice(item.getTotalPrice());

            itemResponses.add(dto);
        }

        response.setItems(itemResponses);

        return response;
    }
    public List<BillResponse> getAllBills() {

        List<Bill> bills = billRepository.findAll();

        List<BillResponse> responses = new ArrayList<>();

        for (Bill bill : bills) {

            BillResponse response = mapToResponse(bill);

            List<BillItem> billItems =
                    billItemRepository.findByBillId(bill.getBillId());

            List<BillItemResponse> itemResponses = new ArrayList<>();

            for (BillItem item : billItems) {

                BillItemResponse dto = new BillItemResponse();

                dto.setProductId(item.getProductId());
                dto.setProductName(item.getProductName());
                dto.setQuantity(item.getQuantity());
                dto.setUnitPrice(item.getUnitPrice());
                dto.setDiscount(item.getDiscount());
                dto.setGstPercentage(item.getGstPercentage());
                dto.setTotalPrice(item.getTotalPrice());

                itemResponses.add(dto);
            }

            response.setItems(itemResponses);

            responses.add(response);
        }

        return responses;
    }
    public BillResponse getBillByInvoice(String invoiceNumber) {

        Bill bill = billRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() ->
                        new RuntimeException("Invoice not found"));

        BillResponse response = mapToResponse(bill);

        List<BillItem> billItems = billItemRepository.findByBillId(bill.getBillId());

        List<BillItemResponse> itemResponses = new ArrayList<>();

        for (BillItem item : billItems) {

            BillItemResponse dto = new BillItemResponse();

            dto.setProductId(item.getProductId());
            dto.setProductName(item.getProductName());
            dto.setQuantity(item.getQuantity());
            dto.setUnitPrice(item.getUnitPrice());
            dto.setDiscount(item.getDiscount());
            dto.setGstPercentage(item.getGstPercentage());
            dto.setTotalPrice(item.getTotalPrice());

            itemResponses.add(dto);
        }

        response.setItems(itemResponses);

        return response;
    }
    private Customer validateCustomer(Integer customerId) {

        if (customerId == null) {
            return null;
        }

        return customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));
    }
    private User validateUser(Integer userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }
    private String generateInvoiceNumber() {

        String date = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String invoice;

        do {

            invoice = "INV-" + date + "-"
                    + (System.currentTimeMillis() % 100000);

        } while (billRepository.existsByInvoiceNumber(invoice));

        return invoice;
    }
    private BillResponse mapToResponse(Bill bill) {

        BillResponse response = new BillResponse();

        response.setBillId(bill.getBillId());
        response.setInvoiceNumber(bill.getInvoiceNumber());
        response.setCustomerId(bill.getCustomerId());
        response.setUserId(bill.getUserId());

        response.setSubtotal(bill.getSubtotal());
        response.setDiscount(bill.getDiscount());
        response.setGstAmount(bill.getGstAmount());
        response.setTotalAmount(bill.getTotalAmount());

        response.setPaymentMethod(bill.getPaymentMethod());
        response.setPaymentStatus(bill.getPaymentStatus());

        response.setBillDate(bill.getBillDate());

        return response;
    }
    

}