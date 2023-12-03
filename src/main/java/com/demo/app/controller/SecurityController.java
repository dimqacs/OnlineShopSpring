package com.demo.app.controller;

import com.demo.app.domain.User;
import com.demo.app.model.SignInDTO;
import com.demo.app.model.SignUpDTO;
import com.demo.app.model.UserDTO;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

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

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final List<UserDTO> authenticatedUsers = new ArrayList<>();

    @Autowired
    public SecurityController(@Qualifier("authenticationManager") AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    ResponseEntity<?> signUp(@RequestBody SignUpDTO signUpDTO) {
        logger.info("Trying to signUp.");
        if (userRepository.existsUserByLogin(signUpDTO.getLogin())) {
            logger.error("Incorrect Login");
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

        logger.info("Signed Up with success. User created.");
        return ResponseEntity.ok("Signed Up with success. User created!");
    }

    @PostMapping("/signin")
    ResponseEntity<?> signIn(@RequestBody SignInDTO signInDTO, HttpServletRequest request) throws ChangeSetPersister.NotFoundException {
        Authentication authentication = null;

        logger.info("Trying to authenticate.");

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDTO.getLogin(), signInDTO.getPassword()));
        } catch (BadCredentialsException e) {
            logger.error("Incorrect password.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UserDTO user = userService.findByLogin(signInDTO.getLogin());
        userService.updateLastSeenDate(signInDTO.getLogin());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        authenticatedUsers.add(user);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);

        return ResponseEntity.ok("User " + signInDTO.getLogin() + " successfully authenticated.");
    }

    @PostMapping("/logout")
    ResponseEntity<?> logOut(HttpServletRequest request) {
        logger.info("Trying to logout.");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            authenticatedUsers.removeIf(userDTO -> userDTO.getLogin().equals(authentication.getName()));
            HttpSession session = request.getSession();
            session.invalidate();
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(null);
            logger.info("Logout successful.");
            return ResponseEntity.ok("Logout successful.");
        } catch (Exception e) {
            logger.error("Cannot logout." + e);
            return ResponseEntity.status(500).body("Error during logout");
        }
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAuthenticatedUsers() {
        logger.info("Trying to send info about all authenticated users.");
        try {
            logger.info("Sent info about all authenticated users.");
            return ResponseEntity.ok(authenticatedUsers);
        } catch (Exception e) {
            logger.error("Error retrieving authenticated users.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
