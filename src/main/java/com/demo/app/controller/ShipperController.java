package com.demo.app.controller;

import com.demo.app.domain.Shipper;
import com.demo.app.service.ShipperService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/shipper", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ShipperController {

    private final ShipperService shipperService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @GetMapping("/{id}")
    public ResponseEntity<Shipper> getShipperById(@PathVariable final Long id){
        try {
            logger.info("Sent info about shipper with id " + id);
            return ResponseEntity.ok(shipperService.findById(id));
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.error("Error while sending info about shipper with id " + id + ", ERROR - ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteShipperById(@PathVariable final Long id){
        try {
            logger.info("Deleting shipper with id " + id);
            shipperService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e){
            logger.error("Error while deleting shipper with id " + id + ", ERROR - ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
