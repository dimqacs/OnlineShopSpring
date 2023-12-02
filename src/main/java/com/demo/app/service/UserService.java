package com.demo.app.service;

import com.demo.app.domain.User;
import com.demo.app.model.UserDTO;
import com.demo.app.model.UserDetailsImpl;
import com.demo.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO){
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setLogin(user.getLogin());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream().map(user -> mapToDTO(user, new UserDTO())).toList();
    }

    public UserDTO findById(Long id) throws ChangeSetPersister.NotFoundException {
        final User user = userRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        return mapToDTO(user, new UserDTO());
    }

    public UserDTO findByLogin(String login) throws ChangeSetPersister.NotFoundException {
        final User user = userRepository.findByLogin(login);
        return mapToDTO(user, new UserDTO());
    }

    public void deleteById(Long id){
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
    }

    public void update(Long id, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (userDTO.getRole() != null) {
            User user = optionalUser.get();
            user.setRole(userDTO.getRole());
            userRepository.save(user);
        } else {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        final User user = userRepository.findByLogin(login);
        return UserDetailsImpl.build(user);
    }


}
