package com.campusconnect.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.campusconnect.security.JwtAuthenticationFilter;
import com.campusconnect.service.CustomUserDetailsService;


@Configuration
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomUserDetailsService customUserDetailsService;



    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService customUserDetailsService) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailsService = customUserDetailsService;

    }



    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {


        http

        // CORS
        .cors(cors -> {})


        // Disable CSRF
        .csrf(csrf -> csrf.disable())


        // JWT Session Management
        .sessionManagement(session ->
            session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
            )
        )


        .authorizeHttpRequests(auth -> auth


            // Allow OPTIONS request
            .requestMatchers(
                    HttpMethod.OPTIONS,
                    "/**"
            )
            .permitAll()



            // Authentication APIs
            .requestMatchers(
                    "/api/users/register",
                    "/api/users/login"
            )
            .permitAll()



            // Uploaded Images and ID Cards
            .requestMatchers(
                    "/uploads/**"
            )
            .permitAll()



            // Admin Module
            .requestMatchers(
                    "/api/admin/**"
            )
            .hasRole("ADMIN")



            // Student Module
            .requestMatchers(
                    "/api/students/**"
            )
            .permitAll()



            // Alumni Module
            .requestMatchers(
                    "/api/alumni/**"
            )
            .hasAnyRole(
                    "ALUMNI",
                    "ADMIN"
            )



            // Connection Module
            .requestMatchers(
                    "/api/connections/**"
            )
            .hasAnyRole(
                    "STUDENT",
                    "ALUMNI",
                    "ADMIN"
            )



            // Opportunities
            .requestMatchers(
                    "/api/opportunities/**"
            )
            .permitAll()



            // Referrals
            .requestMatchers(
                    "/api/referrals/**"
            )
            .permitAll()



            // Projects
            .requestMatchers(
                    "/api/projects/**"
            )
            .hasAnyRole(
                    "STUDENT",
                    "ADMIN"
            )



            // Remaining APIs
            .anyRequest()
            .authenticated()


        )


        // Custom 401 and 403 response
        .exceptionHandling(exceptions -> exceptions


            .authenticationEntryPoint((request, response, authException) -> {

                response.setStatus(401);
                response.setContentType("application/json");

                response.getWriter().write(
                    "{\"status\":401,"
                    + "\"error\":\"Unauthorized\","
                    + "\"message\":\"No valid authentication token for "
                    + request.getRequestURI()
                    + "\"}"
                );

            })


            .accessDeniedHandler((request, response, accessDeniedException) -> {

                response.setStatus(403);
                response.setContentType("application/json");

                response.getWriter().write(
                    "{\"status\":403,"
                    + "\"error\":\"Forbidden\","
                    + "\"message\":\"Access denied for "
                    + request.getMethod()
                    + " "
                    + request.getRequestURI()
                    + "\"}"
                );

            })

        )


        .authenticationProvider(
                authenticationProvider()
        )


        .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );



        return http.build();

    }




    @Bean
    public AuthenticationProvider authenticationProvider() {


        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        customUserDetailsService
                );


        provider.setPasswordEncoder(
                passwordEncoder()
        );


        return provider;

    }





    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }







    @Bean
    public CorsConfigurationSource corsConfigurationSource() {


        CorsConfiguration configuration =
                new CorsConfiguration();



        configuration.setAllowedOrigins(
                List.of(
                    "http://localhost:5173",
                    "https://nextgen-campus.netlify.app/"
                )
        );



        configuration.setAllowedMethods(
                List.of(
                    "GET",
                    "POST",
                    "PUT",
                    "DELETE",
                    "OPTIONS"
                )
        );



        configuration.setAllowedHeaders(
                List.of("*")
        );



        configuration.setAllowCredentials(true);



        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();



        source.registerCorsConfiguration(
                "/**",
                configuration
        );



        return source;

    }

}