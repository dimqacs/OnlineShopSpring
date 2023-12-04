package com.demo.app.service;

import com.demo.app.controller.UserController;

import com.demo.app.domain.Product;
import com.demo.app.domain.Purchase;

import com.demo.app.domain.PurchaseItem;
import com.demo.app.domain.User;
import com.demo.app.model.PurchaseDTO;

import com.demo.app.model.PurchaseItemDTO;
import com.demo.app.model.PurchaseRequest;
import com.demo.app.repository.ProductRepository;
import com.demo.app.repository.PurchaseItemRepository;
import com.demo.app.repository.PurchaseRepository;

import com.demo.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@AllArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    private final UserRepository userRepository;

    private final PurchaseItemRepository purchaseItemRepository;

    private final ProductRepository productRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private PurchaseDTO mapToDTO(final Purchase purchase, final PurchaseDTO purchaseDTO) {
        purchaseDTO.setId(purchase.getId());
        purchaseDTO.setStatus(purchase.getStatus());
        purchaseDTO.setTotal(purchase.getTotal());
        purchaseDTO.setCreatedDate(purchase.getCreatedDate());
        purchaseDTO.setUserName(purchase.getUser().getName());
        purchaseDTO.setUserSurname(purchase.getUser().getSurname());
        purchaseDTO.setUserLogin(purchase.getUser().getLogin());
        purchaseDTO.setUserAge(purchase.getUser().getAge());
        return purchaseDTO;
    }

    public PurchaseDTO findById(Long id) throws ChangeSetPersister.NotFoundException {
        final Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        logger.info("Purchase with id " + id + " was successfully send.");
        return mapToDTO(purchase, new PurchaseDTO());
    }

    public void update(Long id, PurchaseDTO purchaseDTO) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(id);

        if (purchaseDTO.getStatus() != null) {
            Purchase purchase = optionalPurchase.get();
            purchase.setStatus(purchaseDTO.getStatus());
            purchaseRepository.save(purchase);
            logger.info("Purchase's status for purchase with id " + id + " was successfully updated.");
        } else {
            throw new EntityNotFoundException("Purchase not found with ID: " + id);
        }
    }

    public void deleteById(Long id) {
        Optional<Purchase> purchase = purchaseRepository.findById(id);

        if (purchase.isPresent()) {
            purchaseRepository.deleteById(id);
            logger.info("Purchase with id " + id + " was successfully deleted.");
        } else {
            throw new EntityNotFoundException("Purchase not found with ID: " + id);
        }
    }

    public List<PurchaseDTO> findByStatus(PurchaseDTO purchaseDTO) {
        List<Purchase> purchases = purchaseRepository.findAllByStatus(purchaseDTO.getStatus());
        logger.info("Sent info about all purchases with status " + purchaseDTO.getStatus() + ".");
        return purchases.stream().map(purchase -> mapToDTO(purchase, new PurchaseDTO())).toList();
    }

    public void createPurchase(PurchaseRequest purchaseRequest, UserDetails userDetails) {

        try {
            Optional<User> optionalUser = userRepository.findByLogin(userDetails.getUsername());

            Purchase purchase = new Purchase();
            purchase.setCreatedDate(LocalDateTime.now());
            purchase.setUser(optionalUser.get());
            purchase.setTotal(purchaseRequest.getPurchaseTotalPrice());
            purchaseRepository.save(purchase);

            for (PurchaseItemDTO purchaseItemDTO : purchaseRequest.getPurchase()) {
                PurchaseItem purchaseItem = new PurchaseItem();
                purchaseItem.setCount(purchaseItemDTO.getCount());
                purchaseItem.setTotalPrice(purchaseItemDTO.getTotalPrice());
                purchaseItem.setPurchase(purchase);
                Optional<Product> product = productRepository.findById(purchaseItemDTO.getId());
                purchaseItem.setProduct(product.get());
                purchaseItemRepository.save(purchaseItem);
            }
            logger.info("Purchase successfully created.");
        } catch (Exception e) {
                logger.error("Can't create purchase, error - ", e);
        }
    }
}
