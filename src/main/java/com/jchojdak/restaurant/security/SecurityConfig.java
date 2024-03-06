package com.jchojdak.restaurant.security;

import com.jchojdak.restaurant.security.jwt.AuthTokenFilter;
import com.jchojdak.restaurant.security.jwt.JwtAuthEntryPoint;
import com.jchojdak.restaurant.security.user.RestaurantUserDetailsService;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig {
    private final RestaurantUserDetailsService userDetailsService;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    @Bean
    public AuthTokenFilter authenticationTokenFilter(){
        return new AuthTokenFilter();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer :: disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",
                                "/restaurant/info",
                                "/products/favorite",
                                "/products/details/**",
                                "/products/all",
                                "/photos/get/**",
                                "/categories/all",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers(
                                "/restaurant/update",
                                "/roles/remove-user-from-role",
                                "/roles/remove-all-users-from-role/**",
                                "/roles/create",
                                "/roles/assign-user-to-role",
                                "/roles/all",
                                "/roles/delete/**",
                                "/products/add",
                                "/products/delete/**",
                                "/products/edit/**",
                                "/photos/add",
                                "/photos/delete/**",
                                "/categories/add",
                                "/categories/delete/**",
                                "/users/delete/**"
                        ).hasRole("ADMIN")
                        .requestMatchers(
                                "/users/search/**",
                                "/users/all"
                        ).hasAnyRole("STAFF", "ADMIN")
                        .requestMatchers(
                                "/orders/update-status/**",
                                "/orders/all",
                                "/orders/details-admin/**"
                        ).hasAnyRole("STAFF", "ADMIN", "KITCHEN")
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
