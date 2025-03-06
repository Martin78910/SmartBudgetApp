package bg.softuni.smartbudgetapp.config;


import bg.softuni.smartbudgetapp.services.impl.SmartBudgetUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final SmartBudgetUserDetailsService userDetailsService;

    public SecurityConfig(SmartBudgetUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Създаваме PasswordEncoder, който ще използваме в UserService и при логин.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Създаваме AuthenticationManager, конфигуриран с нашия userDetailsService и passwordEncoder.
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    /**
     * Основна Security конфигурация (FilterChain).
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Примерна конфигурация
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/", "/about", "/contact", "/users/register", "/login", "/error").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")     // custom login page
                        .usernameParameter("username") // ако в login form името на полето е username=..., иначе email=...
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                // по желание: .oauth2Login(...) // за социални логини
                .csrf(Customizer.withDefaults());

        return http.build();
    }

}
