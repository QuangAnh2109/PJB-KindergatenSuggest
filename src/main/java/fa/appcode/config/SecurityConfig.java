//package fa.appcode.config;
//
//import fa.appcode.common.utils.Constant;
//import fa.appcode.common.utils.QueryConstant;
//import fa.appcode.web.controller.AuthenticationHandler;
//import fa.appcode.web.controller.CustomAuthenticationSuccessHandler;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.provisioning.JdbcUserDetailsManager;
//import org.springframework.security.provisioning.UserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import javax.sql.DataSource;
//@Configuration
//@RequiredArgsConstructor
//public class SecurityConfig {
//    private final CustomAuthenticationSuccessHandler successHandler;
//    private final AuthenticationHandler authenticationHandler;
//
//
//    @Bean
//    public UserDetailsManager userDetailsManager(DataSource dataSource) {
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//        //define query to retrieve a user by username
//        jdbcUserDetailsManager.setUsersByUsernameQuery(QueryConstant.USERS_BY_USERNAME);
//        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(QueryConstant.AUTHORITIES_BY_USERNAME);
//        return jdbcUserDetailsManager;
//    }
//
//    @Bean
//
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(configurer ->
//                        configurer
//                                .requestMatchers("/public/showMyLoginPage", "/public/forgot-password", "public/register", "public/reset-password").anonymous()
//                                .requestMatchers("/", "/user_side/**", "/public/**").permitAll()
//                                .requestMatchers("/user/**").not().hasAnyAuthority(Constant.SCHOOL_OWNER_ROLE, Constant.ADMIN_ROLE)
//                                .requestMatchers("/auth/**").hasAnyAuthority(Constant.PARENT_ROLE, Constant.SCHOOL_OWNER_ROLE, Constant.ADMIN_ROLE)
//                                .requestMatchers("/parent/**").hasAuthority(Constant.PARENT_ROLE)
//                                .requestMatchers("/school-owner/**").hasAuthority(Constant.SCHOOL_OWNER_ROLE)
//                                .requestMatchers("/manager/**").hasAnyAuthority(Constant.SCHOOL_OWNER_ROLE, Constant.ADMIN_ROLE)
//                                .requestMatchers("/admin/**").hasAuthority(Constant.ADMIN_ROLE)
//                                .anyRequest().authenticated()
//                )
//                .formLogin(form ->
//                        form
//                                .loginPage("/public/showMyLoginPage")
//                                .loginProcessingUrl("/authenticateTheUser")
//                                .successHandler(successHandler)
//                                .failureHandler(authenticationHandler))
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID").permitAll()
//                )
//                .exceptionHandling(configurer -> configurer.accessDeniedPage("/public/access-denied"));
//
//        return http.build();
//    }
//
//
//}
package fa.appcode.config;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.QueryConstant;
import fa.appcode.web.controller.AuthenticationHandler;
import fa.appcode.web.controller.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationSuccessHandler successHandler;
    private final AuthenticationHandler authenticationHandler;
    private final DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsManager userDetailsManager() {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(QueryConstant.USERS_BY_USERNAME);
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(QueryConstant.AUTHORITIES_BY_USERNAME);
        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/public/showMyLoginPage", "/public/forgot-password", "/public/register", "/public/reset-password", "/public/verify-account/**").anonymous()
                                .requestMatchers("/", "/user_side/**", "/public/**", "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                                .requestMatchers("/user/**").not().hasAnyAuthority(Constant.SCHOOL_OWNER_ROLE, Constant.ADMIN_ROLE)
                                .requestMatchers("/auth/**").hasAnyAuthority(Constant.PARENT_ROLE, Constant.SCHOOL_OWNER_ROLE, Constant.ADMIN_ROLE)
                                .requestMatchers("/parent/**").hasAuthority(Constant.PARENT_ROLE)
                                .requestMatchers("/school-owner/**").hasAuthority(Constant.SCHOOL_OWNER_ROLE)
                                .requestMatchers("/manager/**").hasAnyAuthority(Constant.SCHOOL_OWNER_ROLE, Constant.ADMIN_ROLE)
                                .requestMatchers("/admin/**").hasAuthority(Constant.ADMIN_ROLE)
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/public/showMyLoginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .successHandler(successHandler)
                                .failureHandler(authenticationHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessUrl("/public/showMyLoginPage?logout")
                        .permitAll()
                )
                .exceptionHandling(configurer ->
                        configurer
                                .accessDeniedPage("/public/access-denied")
                );

        return http.build();
    }
}
