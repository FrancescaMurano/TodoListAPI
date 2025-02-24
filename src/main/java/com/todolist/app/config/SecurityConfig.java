package com.todolist.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private JwtFilter jwtFilter;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Usa BCrypt per il confronto delle password
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return 
            http
                .csrf(customizer -> customizer.disable())
                // .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(HttpMethod.DELETE, "/todos/*").hasRole("ADMIN")
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/register").hasAnyRole("USER", "ADMIN")
                    .anyRequest().authenticated()
                )
                // http.formLogin(Customizer.withDefaults());
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> 
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() { // per la connessione ad un db etc
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder()); // non sicuro, solo ai fini didattici
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    // @Bean
    // public UserDetailsService userDetailsService(){
    //     UserDetails user1 = User
    //                 .withDefaultPasswordEncoder() // ha la password in chiaro, solo per scopo didattico - NON UTILIZZARE
    //                 .username("fmurano")
    //                 .password("fmurano")
    //                 .roles("USER")
    //                 .build();

    //     UserDetails user2 = User
    //                 .withDefaultPasswordEncoder() // ha la password in chiaro, solo per scopo didattico - NON UTILIZZARE
    //                 .username("fmurano2")
    //                 .password("fmurano")
    //                 .roles("ADMIN")
    //                 .build();

    //     return new InMemoryUserDetailsManager(user1, user2);
    // }
}
