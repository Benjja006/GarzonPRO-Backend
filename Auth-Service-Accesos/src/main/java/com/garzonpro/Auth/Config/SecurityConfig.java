    package com.garzonpro.Auth.Config;

    import com.garzonpro.Auth.Security.JwtAuthorizationFilter;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .csrf(csrf -> csrf.disable())
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth -> auth
                            // Endpoints públicos
                            .requestMatchers("/auth/login", "/auth/register").permitAll()
                            .requestMatchers(HttpMethod.PUT, "/auth/usuarios/actualizar/**").permitAll()
                            .requestMatchers(HttpMethod.DELETE, "/auth/usuarios/eliminar/**").permitAll()
                            .requestMatchers("/error").permitAll()
                            // Todo lo demás requiere autenticación
                            .anyRequest().authenticated()

                    )
                    .addFilterBefore(
                            new JwtAuthorizationFilter(),
                            UsernamePasswordAuthenticationFilter.class)
                    .build();
        }
    }