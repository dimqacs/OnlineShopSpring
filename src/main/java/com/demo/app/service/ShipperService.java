package com.demo.app.service;

import com.demo.app.domain.Shipper;
import com.demo.app.repository.ShipperRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ShipperService {

    private final ShipperRepository shipperRepository;

    public Shipper findById(Long id) throws ChangeSetPersister.NotFoundException {
        return shipperRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipper not found with id: " + id));
    }

    public void deleteById(Long id){
        Optional<Shipper> shipper = shipperRepository.findById(id);

        if (shipper.isPresent()) {
            shipperRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Shipper not found with ID: " + id);
        }
    }
}
