package com.microgis.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SpringSecurityConfig {

    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";
    private static final String SWAGGER = "SWAGGER";

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange()
                .pathMatchers(HttpMethod.GET, "/panel-service/**").hasAnyRole(USER, ADMIN)
                .pathMatchers(HttpMethod.POST, "/panel-service/{id}/text/setting",
                        "/panel-service/{id}/phone/setting",
                        "/panel-service/{id}/physical/setting",
                        "/panel-service/{id}/complex/parameters",
                        "/panel-service/{id}/timeZone",
                        "/panel-service/{id}/timeField",
                        "/panel-service/addPanel",
                        "/panel-service/{id}/gprs").hasAnyRole(USER, ADMIN)
                .pathMatchers(HttpMethod.PUT, "/panel-service/updatePanel").hasAnyRole(USER, ADMIN)
                .pathMatchers(HttpMethod.DELETE, "/panel-service/deletePanel/{id}").hasAnyRole(USER, ADMIN)
                .pathMatchers(HttpMethod.POST, "/panel-service/{id}/style",
                        "/panel-service/{id}/schedule/style",
                        "/panel-service/{id}/operationParameters").hasRole(ADMIN)
                .pathMatchers("/swagger-resources/*",
                        "*.html",
                        "/v3/api-docs",
                        "/swagger-ui.html").hasAnyRole(SWAGGER, ADMIN)
                .anyExchange().authenticated()
                .and()
                .httpBasic()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("root").password("{noop}1970y89mgPuUpFv1nE").roles(USER).build();
        UserDetails swagger = User.withUsername("swagger").password("{noop}password").roles(SWAGGER).build();
        UserDetails admin = User.withUsername("admin").password("{noop}admin").roles(ADMIN).build();
        return new MapReactiveUserDetailsService(user, swagger, admin);
    }
}