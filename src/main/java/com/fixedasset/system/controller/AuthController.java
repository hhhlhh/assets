package com.fixedasset.system.controller;

import com.fixedasset.system.dto.AuthResponse;
import com.fixedasset.system.dto.LoginRequest;
import com.fixedasset.system.entity.User;
import com.fixedasset.system.repository.UserRepository;
import com.fixedasset.system.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow();

            String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

            return ResponseEntity.ok(new AuthResponse(token, user));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("用户名或密码错误");
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow();
        return ResponseEntity.ok(user);
    }
}