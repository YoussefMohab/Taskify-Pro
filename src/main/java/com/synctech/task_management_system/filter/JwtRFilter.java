package com.synctech.task_management_system.filter;

import com.synctech.task_management_system.service.JwtService;
import org.apache.commons.lang3.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.synctech.task_management_system.service.JwtRefreshService;
import com.synctech.task_management_system.service.UserService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRFilter extends OncePerRequestFilter {

    @Autowired
    private JwtRefreshService jwtRefreshService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getRequestURI().equals("/api/v1/refresh")) {
            final String refreshTokenHeader = request.getHeader("X-Refresh-Token");
            final String authHeader = request.getHeader("Authorization");

            final String jwt = authHeader.substring(7);
            final String userEmail;

            if (StringUtils.isEmpty(refreshTokenHeader)) {
                filterChain.doFilter(request, response); // If no token, continue with other filters
                return;
            }

            log.debug("JWT - {}", jwt.toString());
            userEmail = jwtService.extractUserName(jwt);

            String refreshToken = refreshTokenHeader;
            log.debug("RJWT - {}", refreshToken.toString());

            String username = jwtRefreshService.extractUserName(refreshToken);

            if (!userEmail.equals(username)) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write("Forbidden: Email in JWT does not match the username in refresh token");
                return;
            }

            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);
            log.debug("User - {}", userDetails);

            // Validate the refresh token
            if (!jwtRefreshService.isTokenValid(refreshToken, userDetails)) {
                return;
            }
        }

        filterChain.doFilter(request, response); // Continue the filter chain
    }
}
