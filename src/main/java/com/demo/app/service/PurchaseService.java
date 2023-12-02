package com.demo.app.service;

import com.demo.app.domain.Product;
import com.demo.app.domain.Purchase;
import com.demo.app.domain.User;
import com.demo.app.model.ProductDTO;
import com.demo.app.model.PurchaseDTO;
import com.demo.app.model.UserDTO;
import com.demo.app.repository.PurchaseRepository;
import com.demo.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;

    private PurchaseDTO mapToDTO(final Purchase purchase, final PurchaseDTO purchaseDTO){
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

    private LocalDateTime createDate(){
        return LocalDateTime.now();
    }

    public PurchaseDTO findById(Long id) throws ChangeSetPersister.NotFoundException {
        final Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        return mapToDTO(purchase, new PurchaseDTO());
    }

    public void update(Long id, PurchaseDTO purchaseDTO) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(id);

        if (purchaseDTO.getStatus() != null) {
            Purchase purchase = optionalPurchase.get();
            purchase.setStatus(purchaseDTO.getStatus());
            purchaseRepository.save(purchase);
        } else {
            throw new EntityNotFoundException("Purchase not found with ID: " + id);
        }
    }

    public void createPurchase(Long id){
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()){
            Purchase purchase = new Purchase();
            purchase.setCreatedDate(createDate());
            purchase.setUser(user.get());
            purchaseRepository.save(purchase);
        } else {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
    }

    public void deleteById(Long id){
        Optional<Purchase> purchase = purchaseRepository.findById(id);

        if (purchase.isPresent()) {
            purchaseRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Purchase not found with ID: " + id);
        }
    }

}
