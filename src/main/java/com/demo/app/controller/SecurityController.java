package com.demo.app.controller;

import com.demo.app.config.JwtCore;
import com.demo.app.domain.User;
import com.demo.app.model.*;
import com.demo.app.repository.UserRepository;
import com.demo.app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class SecurityController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtCore jwtCore;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final List<UserDTO> authenticatedUsers = new ArrayList<>();

    @Autowired
    public SecurityController(@Qualifier("authenticationManager") AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    ResponseEntity<ResponseDTO> signUp(@RequestBody SignUpDTO signUpDTO) {
        logger.info("Trying to signUp.");
        if (userRepository.existsUserByLogin(signUpDTO.getLogin())) {
            logger.error("Login already exists. Can't create user.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Login already exist. Can't create user"));
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

        logger.info("Signed Up with success. User created.");
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.CREATED.value(), "User successfully created."));
    }

    @PostMapping("/signin")
    ResponseEntity<ResponseDTO> signIn(@RequestBody SignInDTO signInDTO, HttpServletRequest request) throws ChangeSetPersister.NotFoundException {
        Authentication authentication = null;

        logger.info("Trying to authenticate.");

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDTO.getLogin(), signInDTO.getPassword()));
        } catch (BadCredentialsException e) {
            logger.error("Incorrect login or password, Error - ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Incorrect login or password"));
        }

        UserDTO user = userService.findByLogin(signInDTO.getLogin());
        authenticatedUsers.add(user);

        userService.updateLastSeenDate(signInDTO.getLogin());

        UserDetails userDetails = userService.loadUserByUsername(signInDTO.getLogin());
        String token = jwtCore.generateToken(userDetails);

        SignInResponse signInResponse = new SignInResponse(token, user.getId(), user.getRole());
        logger.info("User " + signInDTO.getLogin() + " successfully authenticated.");

        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "User " + signInDTO.getLogin() + " successfully authenticated.", signInResponse));
    }

    @PostMapping("/logout")
    ResponseEntity<ResponseDTO> logOut(HttpServletRequest request) {
        logger.info("Trying to logout.");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            authenticatedUsers.removeIf(userDTO -> userDTO.getLogin().equals(authentication.getName()));
            HttpSession session = request.getSession();
            session.invalidate();
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(null);

            logger.info("User " + authentication.getName() + " logged out successful.");
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "User " + authentication.getName() + " logged out successful."));
        } catch (Exception e) {
            logger.error("Cannot logout." + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred during logging out, Error -  \n" + e));
        }
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/users")
    public ResponseEntity<ResponseDTO> getAuthenticatedUsers() {
        logger.info("Trying to send info about all authenticated users.");
        try {
            logger.info("Sent info about all authenticated users.");
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Info about all authenticated users.", authenticatedUsers));
        } catch (Exception e) {
            logger.error("Error retrieving authenticated users.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred during sending info about all authenticated users, Error -  \n" + e));
        }
    }

}
