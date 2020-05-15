package com.simplejourney.securityjwt.controllers;

import com.simplejourney.securityjwt.dto.LoginData;
import com.simplejourney.securityjwt.services.JWTTokenManageService;
import com.simplejourney.securityjwt.services.impl.UserDetailsServiceImpl;
import com.simplejourney.securityjwt.utils.JWTAuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JWTTokenManageService tokenManageService;

    @Autowired
    private JWTAuthenticationUtil jwtAuthenticationUtil;

    @PostMapping("/login")
    public ResponseEntity<String> login(HttpServletRequest request, @RequestBody LoginData data) throws Exception {
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword()));
        } catch (DisabledException ex) {
            throw new Exception("USER_DISABLED", ex);
        } catch (BadCredentialsException ex) {
            throw new Exception("INVALID_CREDENTIALS", ex);
        }

//        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(data.getUsername());
//        final String token = jwtAuthorizationUtil.generateToken(null, userDetails);
//
//        return ResponseEntity.ok(token);


        String ident = jwtAuthenticationUtil.generateIdent(request);
        if (!tokenManageService.has(ident)) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("ident", ident);

            final UserDetails userDetails = this.userDetailsService.loadUserByUsername(data.getUsername());
            final String token = jwtAuthenticationUtil.generateToken(claims, userDetails);

            tokenManageService.add(ident, token);

            return ResponseEntity.ok(token);
        } else {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String ident = jwtAuthenticationUtil.generateIdent(request);
        if (tokenManageService.has(ident)) {
            tokenManageService.remove(ident);
        }

        return ResponseEntity.ok("Logout Succeed");
    }
}
