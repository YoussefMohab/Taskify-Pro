package com.synctech.task_management_system.rest;

import com.synctech.task_management_system.dto.TokenResponse;
import com.synctech.task_management_system.dto.SignInRequest;
import com.synctech.task_management_system.dto.SignUpRequest;
import com.synctech.task_management_system.entity.User;
import com.synctech.task_management_system.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private AuthService authenticationService;

    @PostMapping("/signup")
    public TokenResponse signup(@Valid @RequestBody SignUpRequest request) {
        return authenticationService.signup(request);
    }

    @PostMapping("/signin")
    public TokenResponse signin(@RequestBody SignInRequest request) {
        return authenticationService.signin(request);
    }

    @GetMapping("/refresh")
    public TokenResponse refresh(@AuthenticationPrincipal User user) {
        return authenticationService.refresh(user);
    }
}