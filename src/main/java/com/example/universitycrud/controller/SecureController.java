package com.example.universitycrud.controller;

import com.example.universitycrud.config.JwtCore;
import com.example.universitycrud.model.DTO.SignInRequest;
import com.example.universitycrud.model.DTO.SignUpRequest;
import com.example.universitycrud.model.User;
import com.example.universitycrud.repository.MyUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class SecureController {
    private MyUserRepository myUserRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtCore jwtCore;
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public void setMyUserRepository(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtCore(JwtCore jwtCore) {
        this.jwtCore = jwtCore;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("signup")
    ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        if (myUserRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already occupied");
        }
        if (myUserRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already occupied");
        }
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        myUserRepository.save(user);

        return ResponseEntity.ok("Success");
    }

    @PostMapping("signin")
    ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.getUsername(),
                            signInRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException exception) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtCore.generateToken(authentication);
        return ResponseEntity.ok("Authorized: " + jwtToken);
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        String jwt = authHeader.substring(7);
        String username = jwtCore.getNameFromJwt(jwt);
        String key = jwtCore.getRedisKey(username);
        redisTemplate.delete(key);
        return ResponseEntity.ok("Logged out");
    }
}
