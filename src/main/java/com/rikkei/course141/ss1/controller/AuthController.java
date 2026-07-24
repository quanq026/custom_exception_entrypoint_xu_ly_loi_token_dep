package com.rikkei.course141.ss1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import com.rikkei.course141.ss1.config.JwtProvider;
import com.rikkei.course141.ss1.security.UserPrincipal;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public AuthController(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(body.get("username"), body.get("password")));
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        String token = jwtProvider.generateToken(principal.getUsername(), principal.getUser().getRole());
        return ResponseEntity.ok(Map.of("accessToken", token, "type", "Bearer", "username", principal.getUsername()));
    }
}
