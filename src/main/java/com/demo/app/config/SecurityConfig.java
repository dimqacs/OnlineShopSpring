package com.demo.app.config;

import com.demo.app.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;

    public SecurityConfig(){}

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Primary
    @Bean
    public AuthenticationManagerBuilder configureAuthenticationManagerBuilder(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(request ->
                        new CorsConfiguration().applyPermitDefaultValues())
                )
                .exceptionHandling (exceptions -> exceptions
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .sessionManagement (session -> session
                        .sessionCreationPolicy (SessionCreationPolicy.ALWAYS)
                )
                .authorizeHttpRequests ((authorize) -> authorize
//                        .requestMatchers( "/auth/**").permitAll()
//                        .requestMatchers("/user/**", "/category/**", "/purchase/**","/shipper/**", "/product/**" ).hasAuthority("ADMIN")
//                        .requestMatchers("/onlineShop").hasAnyAuthority("USER","ADMIN")
                        .anyRequest().permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
                        .addLogoutHandler(logoutHandler())
                        .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
                            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                        })
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
        ;
        return http.build();
    }

    @Bean
    public LogoutHandler logoutHandler() {
        return new SecurityContextLogoutHandler();
    }

}
