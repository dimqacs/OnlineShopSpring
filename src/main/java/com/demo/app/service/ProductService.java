package com.demo.app.service;

import com.demo.app.controller.UserController;
import com.demo.app.domain.Category;
import com.demo.app.domain.Product;
import com.demo.app.domain.Shipper;
import com.demo.app.model.ProductDTO;
import com.demo.app.model.ProductSmallDTO;
import com.demo.app.repository.CategoryRepository;
import com.demo.app.repository.ProductRepository;
import com.demo.app.repository.ShipperRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ShipperRepository shipperRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private ProductDTO mapToDTO(final Product product, final ProductDTO productDTO) {
        productDTO.setId(product.getId());
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

    private ProductSmallDTO mapToSmallDTO(final Product product, final ProductSmallDTO productSmallDTO) {
        productSmallDTO.setId(product.getId());
        productSmallDTO.setName(product.getName());
        return productSmallDTO;
    }

    public ProductDTO findById(Long id) throws ChangeSetPersister.NotFoundException {
        final Product product = productRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        return mapToDTO(product, new ProductDTO());
    }

    public void deleteById(Long id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            productRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Product not found with ID: " + id);
        }
    }

    public void createProduct(ProductDTO productDTO) {
        Product product = new Product();

        product.setName(productDTO.getName());
        product.setYearOfProduction(productDTO.getYearOfProduction());
        product.setDetails(productDTO.getDetails());
        product.setPrice(productDTO.getPrice());
        product.setCount(productDTO.getCount());
        Category category = categoryRepository.findByName(productDTO.getCategoryName());
        product.setCategory(category);
        Shipper shipper = shipperRepository.findByName(productDTO.getShipperName());
        product.setShipper(shipper);
        productRepository.save(product);

        logger.info("Product created.");
    }

    public List<ProductDTO> findAll() {
        final List<Product> products = productRepository.findAll(Sort.by("id"));
        return products.stream().map(product -> mapToDTO(product, new ProductDTO())).toList();
    }


    public ProductDTO findByName(String name) throws ChangeSetPersister.NotFoundException {
        final Optional<Product> product = productRepository.findByName(name);
        return mapToDTO(product.get(), new ProductDTO());
    }


    public List<ProductSmallDTO> findAllProducts(){
        List<Product> products = productRepository.findAll(Sort.by("id"));
        return products.stream().map(product -> mapToSmallDTO(product, new ProductSmallDTO())).toList();
    }

}
