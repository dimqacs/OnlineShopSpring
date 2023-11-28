package com.demo.app.service;

import com.demo.app.domain.Product;
import com.demo.app.model.ProductDTO;
import com.demo.app.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private ProductDTO mapToDTO(final Product product,final ProductDTO productDTO){
        productDTO.setName(product.getName());
        productDTO.setYearOfProduction(product.getYearOfProduction());
        productDTO.setDetails(product.getDetails());
        productDTO.setPrice(product.getPrice());
        productDTO.setCount(product.getCount());
        productDTO.setCategoryName(product.getCategory().getName());
        productDTO.setShipperName(product.getShipper().getName());
        productDTO.setShipperCountry(product.getShipper().getCountry());
        productDTO.setShipperYearOfFoundation(product.getShipper().getFoundationYear());
        productDTO.setShipperDirectorName(product.getShipper().getDirectorName());
        return productDTO;
    }

    public ProductDTO findById(Long id) throws ChangeSetPersister.NotFoundException {
        final Product product = productRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        return mapToDTO(product, new ProductDTO());
    }

    public void deleteById(Long id){
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            productRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Product not found with ID: " + id);
        }
    }
}
