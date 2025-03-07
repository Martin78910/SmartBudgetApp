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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
            .requestMatchers("/", "/about", "/contact", "/users/register", "/users/login", "/error").permitAll()

            // само логнати потребители имат достъп до профил, сметки, бюджети, транзакции
            .requestMatchers("/users/profile").authenticated()
            .requestMatchers("/users/accounts").authenticated()
            .requestMatchers("/users/budgets").authenticated()
            .requestMatchers("/users/transactions").authenticated()

            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/users/login")
           
            .usernameParameter("email")
            .passwordParameter("password")
            // след успешен логин -> "/users/home"
            .defaultSuccessUrl("/users/home", true)
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .permitAll()
        )
        .csrf().disable();

    return http.build();
}


}
