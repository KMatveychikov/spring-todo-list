package matvey.springtodolist.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource)
                .and()
                .csrf().disable()
                .authorizeHttpRequests()
//                .requestMatchers("/api/v1/auth/**").permitAll()
//                .requestMatchers("/api/v1/**").hasAuthority("_ADMIN")
//                .requestMatchers("/api/v1/**").hasAuthority("_USER")
                .requestMatchers("/api/v1/**").permitAll()
                .requestMatchers("/chat/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    CorsConfigurationSource corsConfigurationSource = request -> {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setMaxAge(36000L*36000L);
        return config;
    };


}
