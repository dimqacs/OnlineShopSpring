package com.demo.app.service;

import com.demo.app.controller.UserController;
import com.demo.app.domain.Shipper;
import com.demo.app.repository.ShipperRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class ShipperService {

    private final ShipperRepository shipperRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    public Shipper findById(Long id) throws ChangeSetPersister.NotFoundException {
            Shipper shipper = shipperRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Shipper not found with ID: " + id));
            logger.info("Shipper with ID" + id + " retrieved successfully.");
            return shipper;
    }

    public void deleteById(Long id) {
        try {
            shipperRepository.deleteById(id);
            logger.info("Shipper with id " + id + " successfully deleted.");
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Shipper not found with ID: " + id, e);
        }
    }

    public void createShipper(Shipper shipper){
        try {
            Shipper shipper1 = new Shipper();
            shipper1.setName(shipper.getName());
            shipper1.setCountry(shipper.getCountry());
            shipper1.setFoundationYear(shipper.getFoundationYear());
            shipper1.setDirectorName(shipper.getDirectorName());
            shipperRepository.save(shipper1);
            logger.info("Shipper successfully created.");
        } catch(DataIntegrityViolationException e) {
            throw new RuntimeException("Error while creating shipper, Error - ",e);
        }
    }
}
