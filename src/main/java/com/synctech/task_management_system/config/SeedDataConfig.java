package com.synctech.task_management_system.config;

import com.synctech.task_management_system.entity.Role;
import com.synctech.task_management_system.entity.User;
import com.synctech.task_management_system.repository.UserRepository;
import com.synctech.task_management_system.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        
      if (userRepository.count() == 0) {

        User admin = User
                      .builder()
                      .name("admin")
                      .email("admin@admin.com")
                      .password(passwordEncoder.encode("password"))
                      .role(Role.ROLE_ADMIN)
                      .build();

        userService.save(admin);
        log.debug("created ADMIN user - {}", admin);
      }
    }

}
