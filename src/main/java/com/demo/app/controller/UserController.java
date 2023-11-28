package com.demo.app.controller;

import com.demo.app.model.UserDTO;
import com.demo.app.repository.UserRepository;
import com.demo.app.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController {

    private UserRepository userRepository;
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        try {
            logger.info("Sent info about all users");
            return ResponseEntity.ok(userService.findAll());
        } catch (Exception e) {
            logger.error("Error while sending all users, ERROR - ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable final Long id){
        try {
            logger.info("Sent info about user with id " + id);
            return ResponseEntity.ok(userService.findById(id));
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.error("Error while sending info about user with id " + id + ", ERROR - ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<UserDTO> getUserByLogin(@PathVariable final String login){
        try {
            logger.info("Sent info about user with login " + login);
            return ResponseEntity.ok(userService.findByLogin(login));
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.error("Error while sending info about user with login " + login + ", ERROR - ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable final Long id){
        try {
            logger.info("Deleting user with id " + id);
            userService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e){
            logger.error("Error while deleting user with id " + id + ", ERROR - ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateUserStatus(@PathVariable final Long id , @RequestBody @Validated final UserDTO userDTO){
        try {
            logger.info("Trying to update status for user with id " + id);
            userService.update(id, userDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e){
            logger.error("Error while updating status for user with id " + ", ERROR - ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
