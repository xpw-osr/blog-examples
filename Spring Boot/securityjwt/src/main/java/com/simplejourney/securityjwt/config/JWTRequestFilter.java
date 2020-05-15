package com.simplejourney.securityjwt.config;

import com.simplejourney.securityjwt.services.JWTTokenManageService;
import com.simplejourney.securityjwt.services.impl.UserDetailsServiceImpl;
import com.simplejourney.securityjwt.utils.JWTAuthenticationUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailService;

    @Autowired
    private JWTAuthenticationUtil jwtAuthenticationUtil;

    @Autowired
    private JWTTokenManageService tokenManageService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authorization = httpServletRequest.getHeader("Authorization");

        String username = null;
        String token = null;

        if (null != authorization && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);

            try {
                username = jwtAuthenticationUtil.getUsername(token);
            } catch (IllegalArgumentException ex) {
                System.err.println("IllegalArgumentException: ");
                ex.printStackTrace();
            } catch (JwtException ex) {
                System.err.println("JwtException: ");
                ex.printStackTrace();
            }
        } else {
            System.err.println("Token not found or not start with 'Bearer'");
        }

//        if (null != username && null == SecurityContextHolder.getContext().getAuthentication()) {
//            UserDetails userDetails = this.userDetailService.loadUserByUsername(username);
//
//            if (jwtAuthorizationUtil.validateToken(token, userDetails)) {
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
//                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//            }
//        }

        String ident = jwtAuthenticationUtil.generateIdent(httpServletRequest);
        if (null != username) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean hasIdent = tokenManageService.has(ident);
            if (hasIdent && null == authentication) {
                UserDetails userDetails = this.userDetailService.loadUserByUsername(username);

                if (jwtAuthenticationUtil.isTokenExpired(token)) {
                    tokenManageService.remove(ident);
                }

                if (ident.equals(jwtAuthenticationUtil.getIdent(token)) && jwtAuthenticationUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
