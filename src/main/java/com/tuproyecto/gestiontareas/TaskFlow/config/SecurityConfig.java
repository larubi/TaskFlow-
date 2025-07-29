package com.tuproyecto.gestiontareas.TaskFlow.config;

import com.tuproyecto.gestiontareas.TaskFlow.security.jwt.JwtAuthenticationEntryPoint;
import com.tuproyecto.gestiontareas.TaskFlow.security.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuración de seguridad para la aplicación Spring Boot con JWT.
 * Deshabilita la protección CSRF, configura CORS y establece la autenticación JWT.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Mantenemos JwtAuthenticationEntryPoint aquí porque no suele participar en el ciclo principal de este tipo
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            // Inyectamos UserDetailsService y JwtRequestFilter como parámetros del método @Bean
            // Esto permite que Spring los resuelva de forma diferente y rompa el ciclo
            UserDetailsService jwtUserDetailsService, // Spring encontrará el bean de JwtUserDetailsService
            JwtRequestFilter jwtRequestFilter) throws Exception { // Spring encontrará el bean de JwtRequestFilter

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        // 1. URLs de acceso público que no requieren autenticación
                        .requestMatchers("/api/auth/login").permitAll() // Endpoint de autenticación
                        .requestMatchers("/api/auth/register").permitAll()   // Endpoint de registro
                        .requestMatchers(
                                "/swagger-ui.html",    // Página principal de Swagger UI
                                "/swagger-ui/**",      // Recursos estáticos de Swagger UI
                                "/v3/api-docs/**",     // Endpoint de la especificación OpenAPI v3
                                "/webjars/**",         // Recursos de Webjars (comúnmente usados por Swagger)
                                "/api-docs/**"         // Otros endpoints de docs de API (versiones anteriores)
                        ).permitAll() // Permite el acceso a la documentación de Swagger/OpenAPI

                        // Para que H2 Console sea público solo para desarrollo
                        .requestMatchers("/h2-console/**").permitAll()

                        // 2. URLs que requieren autenticación
                        .requestMatchers("/api/**").authenticated() // Todas las peticiones por debajo, requieren autenticación

                        // 3. Cualquier otra petición que no haya sido casada por las reglas anteriores
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Este bean también necesita el UserDetailsService. Lo pasamos como parámetro aquí también.
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService jwtUserDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(jwtUserDetailsService); // Usa la instancia inyectada en el método
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}