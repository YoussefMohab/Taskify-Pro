package com.synctech.task_management_system.rest;

import com.synctech.task_management_system.entity.User;
import com.synctech.task_management_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @PatchMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User makeManager(@PathVariable String id) {
        return userService.updateRole(id);
    }
}
