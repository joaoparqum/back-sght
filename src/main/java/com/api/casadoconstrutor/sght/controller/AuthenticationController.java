package com.api.casadoconstrutor.sght.controller;

import com.api.casadoconstrutor.sght.dto.AuthenticationDto;
import com.api.casadoconstrutor.sght.dto.LoginDto;
import com.api.casadoconstrutor.sght.dto.RegisterDto;
import com.api.casadoconstrutor.sght.infra.security.TokenService;
import com.api.casadoconstrutor.sght.repository.UserRepository;
import com.api.casadoconstrutor.sght.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sght/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDto data){
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
            var auth = authenticationManager.authenticate(usernamePassword);
            var user = (User) auth.getPrincipal();
            var token = tokenService.generateToken(user);
            System.out.println("Token gerado: " + token);

            return ResponseEntity.ok(new LoginDto(token, user.getRole().getRole(), user.getLogin()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erro: Credenciais inválidas.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDto data){
        System.out.println("Login: " + data.login());
        System.out.println("Password: " + data.password());
        System.out.println("Role: " + data.role());
        if(this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.login(), encryptedPassword, data.role());

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }



}
