package fa.appcode.security;

import fa.appcode.common.utils.Constant;
import fa.appcode.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;
    private final AuthenticationHandler authenticationHandler;
    private final AccountRepository accountRepository;
    private final SessionRegistry sessionRegistry;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new AccountInfoDetailsService(accountRepository);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public static HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/forgot-password", "/public/register", "/public/reset-password", "/public/verify-account/**").anonymous()
                        .requestMatchers("/", "/user_side/**", "/public/**", "/resources/**", "/static/**", "/css/**","/api/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/user/**").not().hasAnyAuthority(Constant.SCHOOL_OWNER_ROLE, Constant.ADMIN_ROLE)
                        .requestMatchers("/auth/**").hasAnyAuthority(Constant.PARENT_ROLE, Constant.SCHOOL_OWNER_ROLE, Constant.ADMIN_ROLE)
                        .requestMatchers("/parent/**").hasAuthority(Constant.PARENT_ROLE)
                        .requestMatchers("/school-owner/**").hasAuthority(Constant.SCHOOL_OWNER_ROLE)
                        .requestMatchers("/manager/**").hasAnyAuthority(Constant.SCHOOL_OWNER_ROLE, Constant.ADMIN_ROLE)
                        .requestMatchers("/admin/**").hasAuthority(Constant.ADMIN_ROLE)
                        .anyRequest().authenticated()
                ).formLogin(form -> form
                        .loginPage("/public/sign-in")
                        .loginProcessingUrl("/authenticateTheUser")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(successHandler)
                        .failureHandler(authenticationHandler).permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessUrl("/public/sign-in?logout")
                        .permitAll()
                ).sessionManagement(session -> session
                        .invalidSessionUrl("/public/sign-in?timeout=true")
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .expiredUrl("/public/showMyLoginPage?expired=true")
                        .sessionRegistry(sessionRegistry)
                )
                .exceptionHandling(configurer -> configurer
                        .accessDeniedPage("/public/access-denied")
                );

        return http.build();
    }
}
