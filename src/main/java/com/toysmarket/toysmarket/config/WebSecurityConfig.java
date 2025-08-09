package com.toysmarket.toysmarket.config;

import com.toysmarket.toysmarket.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.nio.file.AccessDeniedException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        //Доступ только для не зарегистрированных пользователей
                        .requestMatchers("/registration").not().authenticated()
                        .requestMatchers("/market", "/resources/**").permitAll()
                        .requestMatchers("/guest").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {
                            HttpSession session = request.getSession(false);
                            String targetUrl = (session != null) ? (String) session.getAttribute("targetUrl") : null;

                            if (targetUrl != null && !targetUrl.isEmpty()) {
                                response.sendRedirect(targetUrl);
                                session.removeAttribute("targetUrl");
                            } else {
                                response.sendRedirect("/market");
                            }
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .permitAll()
                        .logoutSuccessHandler((request, response, authentication) -> {
                            String referer = request.getHeader("Referer");
                            if (referer != null && !referer.contains("/login")) {
                                response.sendRedirect(referer);
                            } else {
                                response.sendRedirect("/market");
                            }
                        })
                )
                .exceptionHandling(exception -> exception
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    if (request.getRequestURI().equals("/registration")) {
                        response.sendRedirect("/market");
                    } else {
                        throw accessDeniedException;
                    }
                })
        );
        return http.build();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }
}
