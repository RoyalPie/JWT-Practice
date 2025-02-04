package com.example.JWT.config;

import com.example.JWT.filter.JWTFilter;
import com.example.JWT.service.StaffDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private JWTFilter jwtFilter;

    private final StaffDetailService staffDetailService;

    public SecurityConfig(StaffDetailService staffDetailService) {
        this.staffDetailService = staffDetailService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(
                        authorize -> {
                            authorize.requestMatchers("/css/**", "/js/**", "/images/**").permitAll();
                            authorize.requestMatchers("/auth/login", "/error/**", "/logout", "/", "/auth/signup").permitAll();
                            // Restrict access to admin and user pages based on roles
                            authorize.requestMatchers("/admin/**").hasRole("ADMIN");
                            authorize.requestMatchers("/user/**").hasRole("USER");
                            // All other requests require authentication
                            authorize.anyRequest().authenticated();
                        }
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(formLogin -> formLogin
                        .loginPage("/auth/login")  // Custom login page
                        .defaultSuccessUrl("/success", true)  // Redirect to home after successful login
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")  // Redirect to login page after logout
                        .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF for simplicity (not recommended for production)
                .build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(staffDetailService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

