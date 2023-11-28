package com.demo.app.service;

import com.demo.app.domain.Product;
import com.demo.app.domain.Purchase;
import com.demo.app.model.ProductDTO;
import com.demo.app.model.PurchaseDTO;
import com.demo.app.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    private PurchaseDTO mapToDTO(final Purchase purchase, final PurchaseDTO purchaseDTO){
        purchaseDTO.setId(purchase.getId());
        purchaseDTO.setStatus(purchase.getStatus());
        purchaseDTO.setTotal(purchase.getTotal());
        purchaseDTO.setCreatedDate(purchase.getCreatedDate());
        purchaseDTO.setUserName(purchase.getUser().getName());
        purchaseDTO.setUserSurname(purchase.getUser().getSurname());
        purchaseDTO.setUserAge(purchase.getUser().getAge());
        return purchaseDTO;
    }

    public PurchaseDTO findById(Long id) throws ChangeSetPersister.NotFoundException {
        final Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        return mapToDTO(purchase, new PurchaseDTO());
    }
}
