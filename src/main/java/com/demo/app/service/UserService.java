package com.demo.app.service;

import com.demo.app.controller.UserController;
import com.demo.app.domain.User;
import com.demo.app.model.UserDTO;
import com.demo.app.model.UserDetailsImpl;
import com.demo.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setLogin(user.getLogin());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        logger.info("Info about all users was successfully send.");
        return users.stream().map(user -> mapToDTO(user, new UserDTO())).toList();
    }

    public UserDTO findById(Long id) throws ChangeSetPersister.NotFoundException {
        final User user = userRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        logger.info("Info about user with id " + id + " was successfully send.");
        return mapToDTO(user, new UserDTO());
    }

    public UserDTO findByLogin(String login) throws ChangeSetPersister.NotFoundException {
        final Optional<User> optionalUser = userRepository.findByLogin(login);
        logger.info("Info about user with login " + login + " was successfully send.");
        if(optionalUser.isPresent())
            return mapToDTO(optionalUser.get(), new UserDTO());
        else
            throw new ChangeSetPersister.NotFoundException();
    }


    public void deleteById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            userRepository.deleteById(id);
            logger.info("user with id " + id + " was successfully deleted.");
        } else {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
    }

    public void updateRole(String login, String role) {
        Optional<User> optionalUser = userRepository.findByLogin(login);

        if (optionalUser.isPresent()) {
            optionalUser.get().setRole(role);
            userRepository.save(optionalUser.get());
            logger.info("For user with login " + login + ", role was successfully updated.");
        } else {
            throw new EntityNotFoundException("User not found with login: " + login);
        }
    }

    public void updateLastSeenDate(String login) {
        Optional<User> optionalUser = userRepository.findUserByLogin(login);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setLastSeenDate(LocalDateTime.now());
            userRepository.save(user);
            logger.info("Last seen date for user with login " + login + " was successfully updated.");
        } else {
            throw new EntityNotFoundException("User not found with login: " + login);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        final Optional<User> optionalUser = userRepository.findByLogin(login);
        return UserDetailsImpl.build(optionalUser.get());
    }


}
