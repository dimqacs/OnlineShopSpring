package com.demo.app.controller;

import com.demo.app.domain.User;
import com.demo.app.model.SignInDTO;
import com.demo.app.model.SignUpDTO;
import com.demo.app.model.UserDTO;
import com.demo.app.repository.UserRepository;
import com.demo.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class SecurityController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    public SecurityController(@Qualifier("authenticationManager") AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    ResponseEntity<?> signUp(@RequestBody SignUpDTO signUpDTO){
        if (userRepository.existsUserByLogin(signUpDTO.getLogin())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect Login");
        }

        String hashed = passwordEncoder.encode(signUpDTO.getPassword());

        User user = new User();
        user.setName(signUpDTO.getName());
        user.setSurname(signUpDTO.getSurname());
        user.setAge(signUpDTO.getAge());
        user.setLogin(signUpDTO.getLogin());
        user.setPassword(hashed);
        user.setRegistredDate(LocalDateTime.now());
        userRepository.save(user);

        return ResponseEntity.ok("Signed Up with succes. User created!");
    }

    @PostMapping("/signin")
    ResponseEntity<?> signIn(@RequestBody SignInDTO signInDTO) throws ChangeSetPersister.NotFoundException {
        Authentication authentication = null;

        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDTO.getLogin(), signInDTO.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
