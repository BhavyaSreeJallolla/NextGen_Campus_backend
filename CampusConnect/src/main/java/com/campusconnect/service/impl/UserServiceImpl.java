package com.campusconnect.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.campusconnect.dto.LoginRequest;
import com.campusconnect.dto.LoginResponse;
import com.campusconnect.entity.User;
import com.campusconnect.enums.Role;
import com.campusconnect.enums.VerificationStatus;
import com.campusconnect.exception.EmailAlreadyExistsException;
import com.campusconnect.exception.InvalidPasswordException;
import com.campusconnect.exception.InvalidTokenException;
import com.campusconnect.exception.UserNotFoundException;
import com.campusconnect.repository.UserRepository;
import com.campusconnect.security.JwtService;
import com.campusconnect.service.UserService;

import java.time.LocalDateTime;
import java.util.UUID;

import com.campusconnect.entity.PasswordResetToken;
import com.campusconnect.repository.PasswordResetTokenRepository;
import com.campusconnect.service.EmailService;

@Service
public class UserServiceImpl implements UserService {



    @Autowired
    private UserRepository userRepository;



    @Autowired
    private JwtService jwtService;



    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailService emailService;
    @Override
    public User register(User user) {


        if(userRepository.existsByEmail(user.getEmail())){
        	throw new EmailAlreadyExistsException("Email already exists.");


        }




        if(user.getRole() == Role.ADMIN){


            user.setStatus(
                    VerificationStatus.VERIFIED
            );


        }
        else{


            user.setStatus(
                    VerificationStatus.PENDING
            );


        }




        user.setPassword(

                passwordEncoder.encode(
                        user.getPassword()
                )

        );




        return userRepository.save(user);


    }
    @Override
    public LoginResponse login(LoginRequest request) {



        User user =
                userRepository.findByEmail(
                        request.getEmail()
                )
                .orElseThrow(

                        () -> new UserNotFoundException(
                                "User not found"
                        )

                );






        if(!passwordEncoder.matches(

                request.getPassword(),

                user.getPassword()

        )){


            throw new InvalidPasswordException(
                    "Invalid Password"
            );


        }







        String token =

                jwtService.generateToken(

                        user.getEmail(),

                        user.getRole().name()
                );
        LoginResponse response =
                new LoginResponse();
        // IMPORTANT FOR REACT STUDENT PROFILE

        response.setId(
                user.getId()
        );
        response.setToken(
                token
        );
        response.setRole(
                user.getRole().name()
        );
        response.setName(
                user.getName()
        );
        response.setEmail(
                user.getEmail()
        );
        response.setMessage(
                "Login Successful"
        );
        return response;
    }
    @Override
    public void forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Email not found"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByUser(user)
                .orElse(new PasswordResetToken());

        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));

        passwordResetTokenRepository.save(resetToken);

        String resetLink =
                "http://localhost:5173/reset-password?token=" + token;

        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
    }
    @Override
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken =
                passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new InvalidTokenException("Invalid Token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Token Expired");
        }

        User user = resetToken.getUser();

        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }
}