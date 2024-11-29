package com.synctech.task_management_system.service;

import com.synctech.task_management_system.dto.SignInRequest;
import com.synctech.task_management_system.dto.SignUpRequest;
import com.synctech.task_management_system.dto.TokenResponse;
import com.synctech.task_management_system.entity.Role;
import com.synctech.task_management_system.entity.User;
import com.synctech.task_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtRefreshService jwtRefreshService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public TokenResponse signup(SignUpRequest request) {
        var user = User
                .builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_EMPLOYEE)
                .build();

        user = userService.save(user);
        var jwt = jwtService.generateToken(user);
        return TokenResponse.builder().accessToken(jwt).refreshToken("").build();
    }


    public TokenResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        var rJwt = jwtRefreshService.generateToken(user);
        return TokenResponse.builder().accessToken(jwt).refreshToken(rJwt).build();
    }

    public TokenResponse refresh(User userDetails) {
        var user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        var jwt = jwtService.generateToken(user);
        return TokenResponse.builder().accessToken(jwt).refreshToken("").build();
    }
}
