package com.campusconnect.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import com.campusconnect.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {



    @Autowired
    private JwtService jwtService;


    @Autowired
    private CustomUserDetailsService userDetailsService;



    @Override
    protected void doFilterInternal(

            HttpServletRequest request,

            HttpServletResponse response,

            FilterChain filterChain)

            throws ServletException, IOException {



        // Allow OPTIONS request
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {

            filterChain.doFilter(request, response);
            return;
        }



        String path = request.getServletPath();



        // Skip authentication endpoints
        if (path.equals("/api/users/login")
                || path.equals("/api/users/register")) {

            filterChain.doFilter(request, response);
            return;
        }





        String authHeader = request.getHeader("Authorization");


        System.out.println("JWT HEADER : " + authHeader);



        // No token
        if (authHeader == null 
                || !authHeader.startsWith("Bearer ")) {


            filterChain.doFilter(request, response);
            return;

        }




        String token = authHeader.substring(7);



        // Empty token check
        if(token.isBlank()) {

            filterChain.doFilter(request,response);
            return;

        }





        try {


            String email =
                    jwtService.extractUsername(token);



            System.out.println(
                    "JWT EMAIL : " + email
            );



            if(email != null &&

              SecurityContextHolder
              .getContext()
              .getAuthentication() == null) {



                UserDetails userDetails =
                        userDetailsService
                        .loadUserByUsername(email);



                if(jwtService.validateToken(
                        token,
                        email)) {



                    UsernamePasswordAuthenticationToken authentication =

                            new UsernamePasswordAuthenticationToken(

                                    userDetails,

                                    null,

                                    userDetails.getAuthorities()

                            );



                    authentication.setDetails(

                            new WebAuthenticationDetailsSource()
                            .buildDetails(request)

                    );



                    SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);


                }


            }



        }
        catch(Exception e) {


            System.out.println(
                    "INVALID JWT TOKEN : "
                    + e.getMessage()
            );


            // Clear invalid authentication
            SecurityContextHolder
            .clearContext();

        }




        filterChain.doFilter(request,response);


    }

}