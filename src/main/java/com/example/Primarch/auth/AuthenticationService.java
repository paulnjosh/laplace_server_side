package com.example.Primarch.auth;

import com.example.Primarch.Config.JwtService;
import com.example.Primarch.Token.Token;
import com.example.Primarch.Token.TokenRepository;
import com.example.Primarch.Token.TokenType;
import com.example.Primarch.User.User;
import com.example.Primarch.User.UserRepository;
import com.example.Primarch.UserProfile.UserProfile;
import com.example.Primarch.UserProfile.UserProfileRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final UserProfileRepo userProfileRepo;

    public AuthenticationResponse register(RegisterRequest request) {
        // TODO: 2/11/2024 validate email adderess
        System.out.println(validateEmail(request.getEmail()));
        if(!validateEmail(request.getEmail())){
            return AuthenticationResponse.builder()
                    .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                    .message("Invalid email address")
                    .build();
        }
        // TODO: 2/11/2024 check if the user email exists
        if(repository.existsByEmail(request.getEmail())){
            return AuthenticationResponse.builder()
                    .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                    .message("User with email "+request.getEmail()+" is already registered")
                    .build();
        }
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        System.out.println("About to save user");
        var savedUser = repository.save(user);
        var userProfile= UserProfile.builder()
                .email(savedUser.getEmail())
                .build();
        userProfileRepo.save(userProfile);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .message("User registered successfully")
                .statusCode(HttpStatus.CREATED.value())
                .build();
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .message("Success")
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

//    public  boolean patternMatches(String emailAddress) {
//        String email = "paulnjoroge430@gmail.com";
//        System.out.println("Email before validation: " + email);
//
//        String regexPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
//        boolean result = Pattern.compile(regexPattern).matcher(email).matches();
//
//        System.out.println("Validation result: " + result);
//        return result;
//    }

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);
    public static boolean validateEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}
