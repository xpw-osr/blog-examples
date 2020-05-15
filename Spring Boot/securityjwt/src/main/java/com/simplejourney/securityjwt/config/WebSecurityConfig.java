package com.simplejourney.securityjwt.config;

import com.simplejourney.securityjwt.services.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private DemoAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private JWTRequestFilter requestFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // Disable Cross-Site Request Forgery protection
                .csrf().disable()
                // Enable Cross-Origin Resource Sharing
                .cors().disable()


                /**
                 * Resources which need not authorization
                 */
                .authorizeRequests()
                // all Swagger-UI accessing need not authorization
                .antMatchers(
                        "/v2/api-docs",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui.html/**",
                        "/webjars/**"
                ).permitAll()
                .antMatchers("/hello", "/auth/login").permitAll()


                /**
                 * Other resources need permissions
                 */
                .anyRequest().authenticated()

                /**
                 * Exception Handling
                 */
                .and().exceptionHandling()
                // set process when autentication failed
                .authenticationEntryPoint(authenticationEntryPoint)
                // set handler for access denied
                .accessDeniedHandler(accessDeniedHandler)


                /**
                 * Session
                 */
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // add JWT filter before authentication
        http.addFilterAt(requestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
