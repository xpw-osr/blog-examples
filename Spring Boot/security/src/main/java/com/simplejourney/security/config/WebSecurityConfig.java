package com.simplejourney.security.config;

import com.simplejourney.security.services.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private UserLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private UserAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private DemoAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private DemoSessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    // #################################################
    // for Custom Access Decision - BEGIN
//    @Autowired
//    private DemoAbstractSecurityInterceptor securityInterceptor;
//
//    @Autowired
//    private DemoFilterInvocationSecurityMetadataSource securityMetadataSource;
//
//    @Autowired
//    private DemoAccessDecisionManager accessDecisionManager;
    // for Custom Access Decision - END
    // #################################################


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

    // #################################################
    // # For login with Json data in body - BEGIN
//    UsernamePasswordAuthenticationFilter jsonAuthenticationFilter() throws Exception {
//        JsonAuthenticationFilter filter = new JsonAuthenticationFilter();
//        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
//        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
//        filter.setAuthenticationManager(authenticationManagerBean());
//        return filter;
//    }
    // # For login with Json data in body - END
    // #################################################

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                // Disable Cross-Site Request Forgery protection
                csrf().disable()
                // Enable Cross-Origin Resource Sharing
                .cors().disable()


                /**
                 * Resources which need not authorization
                 */
                .authorizeRequests()
                .antMatchers("/hello", "/auth/login").permitAll()


                /**
                 * Other resources need permissions
                 */
                // #################################################
                // # for basic policies - BEGIN
                .anyRequest().authenticated()
                // # for custom access decision - END
                // #################################################

                // #################################################
                // # for custom access decision - BEGIN
//                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                    @Override
//                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
//                        o.setAccessDecisionManager(accessDecisionManager);//决策管理器
//                        o.setSecurityMetadataSource(securityMetadataSource);//安全元数据源
//                        return o;
//                    }
//                })
                // # for custom access decision - END
                // #################################################


                /**
                 * Login
                 */
                // #################################################
                // # for basic policies - BEGIN
                .and().formLogin()
                // #################################################
                // # redirect to AuthController after authentication succeed - BEGIN
                //.successForwardUrl("/auth/login")
                // # redirect to AuthController after authentication succeed - END
                // #################################################

                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll()
                // # for basic policies - END
                // #################################################


                /**
                 * Logout
                 */
                .and().logout()
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll()
                .deleteCookies("JSESSIONID")


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
                .maximumSessions(1)
                .expiredSessionStrategy(sessionInformationExpiredStrategy);

        // #################################################
        // # for custom access decision - BEGIN
//        http.addFilterBefore(securityInterceptor, FilterSecurityInterceptor.class);
        // # for custom access decision - END
        // #################################################

        // #################################################
        // For login with Json data in body - BEGIN
//        http.addFilterAt(jsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        // For login with Json data in body - END
        // #################################################
    }
}
