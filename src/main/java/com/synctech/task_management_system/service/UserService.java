package com.synctech.task_management_system.service;

import com.synctech.task_management_system.entity.Role;
import com.synctech.task_management_system.entity.User;
import com.synctech.task_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    public User save(User newUser) {
        return userRepository.save(newUser);
    }

    public User updateRole(String id) {
        UUID newId = UUID.fromString(id);

        User user = userRepository.getById(newId);

        user.setRole(Role.ROLE_MANAGER);

        return userRepository.save(user);
    }
}
