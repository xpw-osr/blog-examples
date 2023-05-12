package com.simplejourney.apidocs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.cors().and().csrf().disable()
            .authorizeHttpRequests().antMatchers("/apidocs/**", "/swagger-ui/**", "/swagger-ui.html", "/login", "/").permitAll()
            .and()
            .authorizeHttpRequests().anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .build();
        // http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
        // http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }    
}
