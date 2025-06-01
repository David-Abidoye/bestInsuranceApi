package com.bestinsurance.api.config;

import static com.bestinsurance.api.security.Roles.ADMIN;
import static com.bestinsurance.api.security.Roles.BACK_OFFICE;
import static com.bestinsurance.api.security.Roles.CUSTOMER;
import static com.bestinsurance.api.security.Roles.FRONT_OFFICE;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class ServiceSecurityConfig {

    @Bean
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequestsCustomizer())
                .oauth2ResourceServer(authResourceServer ->
                        authResourceServer.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .csrf(csrf -> csrf.disable())
                .build();
    }

    private Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequestsCustomizer() {
        return auth -> auth
                .requestMatchers(HttpMethod.PUT, "/subscriptions/*/*").hasAnyRole(FRONT_OFFICE, BACK_OFFICE, ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/subscriptions/*/*").hasAnyRole(FRONT_OFFICE, BACK_OFFICE, ADMIN)
                .requestMatchers(HttpMethod.POST, "/subscriptions").hasAnyRole(FRONT_OFFICE, BACK_OFFICE, ADMIN)
                .requestMatchers(HttpMethod.POST, "/subscriptions/upload").hasAnyRole(BACK_OFFICE, ADMIN)
                .requestMatchers(HttpMethod.POST, "/subscriptions/revenues").hasAnyRole(BACK_OFFICE, ADMIN)
                .requestMatchers(HttpMethod.PUT, "/policies/*").hasAnyRole(FRONT_OFFICE, BACK_OFFICE, ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/policies/*").hasAnyRole(FRONT_OFFICE, BACK_OFFICE, ADMIN)
                .requestMatchers(HttpMethod.POST, "/policies").hasAnyRole(FRONT_OFFICE, BACK_OFFICE, ADMIN)
                .requestMatchers(HttpMethod.PUT, "/customers/*").hasAnyRole(CUSTOMER, FRONT_OFFICE, BACK_OFFICE, ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/customers/*").hasAnyRole(FRONT_OFFICE, BACK_OFFICE, ADMIN)
                .requestMatchers(HttpMethod.POST, "/customers").hasAnyRole(CUSTOMER, FRONT_OFFICE, BACK_OFFICE, ADMIN)
                .requestMatchers(HttpMethod.GET, "/customers/subscriptions").hasAnyRole(FRONT_OFFICE, ADMIN)
                .requestMatchers(HttpMethod.GET, "/customers/subscriptions/discountedPrice").hasAnyRole(FRONT_OFFICE, ADMIN)
                .requestMatchers(HttpMethod.GET, "/customers/policy/*").hasAnyRole(FRONT_OFFICE, ADMIN)
                .requestMatchers(HttpMethod.GET, "/customers/coverage/*").hasAnyRole(FRONT_OFFICE, ADMIN)
                .requestMatchers(HttpMethod.PUT, "/coverages/*").hasAnyRole(FRONT_OFFICE, BACK_OFFICE, ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/coverages/*").hasAnyRole(FRONT_OFFICE, BACK_OFFICE, ADMIN)
                .requestMatchers(HttpMethod.POST, "/coverages").hasAnyRole(FRONT_OFFICE, BACK_OFFICE, ADMIN)
                .requestMatchers("/customers/**").authenticated()
                .requestMatchers("/subscriptions/**").authenticated()
                .requestMatchers("/policies/**").authenticated()
                .requestMatchers("/coverages/**").authenticated()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .anyRequest().hasAuthority(ADMIN);
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
