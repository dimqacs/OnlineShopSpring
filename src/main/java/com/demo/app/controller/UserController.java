package com.demo.app.controller;

import com.demo.app.model.ResponseDTO;
import com.demo.app.model.UserDTO;
import com.demo.app.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@EnableGlobalAuthentication
public class UserController {

    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public ResponseEntity<ResponseDTO> getAllUsers() {
        logger.info("Trying to send info about all users");
        try {
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Info about all users", userService.findAll()));
        } catch (Exception e) {
            logger.error("Error while sending info about all users, ERROR - ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while sending info about all users, ERROR - ", e));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getUserById(@PathVariable final Long id) {
        logger.info("Trying to sent info about user with id " + id);
        try {
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Info about user.", userService.findById(id)));
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.error("User with id " + id + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND.value(), "User with id " + id + " not found."));
        } catch (Exception e) {
            logger.error("Error while sending info about user with id " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while sending info about user with id " + id + ", ERROR - " + e));
        }
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<ResponseDTO> getUserByLogin(@PathVariable final String login) {
        logger.info("Trying to send info about user with login " + login);
        try {
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Info about user.", userService.findByLogin(login)));
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.error("User with login " + login + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND.value(), "User with login " + login + " not found."));
        } catch (Exception e) {
            logger.error("Error while sending info about user with login " + login, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while sending info about user with login " + login + ", ERROR - " + e));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteUserById(@PathVariable final Long id) {
        logger.info("Trying to delete user with id " + id);
        try {
            userService.deleteById(id);
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "User with id " + id + " successfully deleted."));
        } catch (EntityNotFoundException e) {
            logger.error("User with id " + id + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND.value(), "User with id " + id + " not found."));
        } catch (Exception e) {
            logger.error("Error while deleting user with id " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred during deletion for user with id " + id + "."));
        }
    }

    @PutMapping("/update/{login}")
    public ResponseEntity<ResponseDTO> updateUserRole(@PathVariable final String login, @RequestBody @Validated final UserDTO userDTO) {
        logger.info("Trying to update role for user with login " + login);
        try {
            userService.updateRole(login, userDTO.getRole());
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "User's role with login " + login + " successfully updated."));
            } catch (Exception e) {
            logger.error("Error while updating role for user with login " + login, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while updating role for user with login " + login + ", error - \n" +  e));
        }
    }

}
