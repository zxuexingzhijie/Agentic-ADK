package com.alibaba.agentic.computer.use.configuration;

import com.google.adk.Version;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = {"com.alibaba.agentic", "com.google.adk"})
@EnableWebSecurity
public class BrowserUseStarter {


    static {
        System.out.println("\n"
                + "  ___  ____  _  __   ____                                 \n"
                + " / _ \\|  _ \\| |/ /  | __ ) _ __ _____      _____  ___ _ __ \n"
                + "| | | | | | | ' /   |  _ \\| '__/ _ \\ \\ /\\ / / __|/ _ \\ '__|\n"
                + "| |_| | |_| | . \\   | |_) | | | (_) \\ V  V /\\__ \\  __/ |   \n"
                + " \\___/|____/|_|\\_\\  |____/|_|  \\___/ \\_/\\_/ |___/\\___|_|   \n"
                + "                                                           \n"
                + "adk version " + Version.JAVA_ADK_VERSION + "\n");
    }

    @Bean
    public SecurityFilterChain filterChainCsrf(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
        return http.build();
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 对所有路径生效
                        .allowedOrigins("*") // 允许所有来源
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization")
                        .allowCredentials(false) // 不允许携带 Cookie
                        .maxAge(3600);;
            }
        };
    }

}
