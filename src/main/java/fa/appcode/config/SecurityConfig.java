package fa.appcode.config;
import fa.appcode.web.controller.AuthenticationHandler;
import fa.appcode.web.controller.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import javax.sql.DataSource;
@Configuration
public class SecurityConfig {
        private final CustomAuthenticationSuccessHandler successHandler;
        @Autowired
        AuthenticationHandler authenticationHandler;

    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        //define query to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT email, password, CASE WHEN status_id = 41 THEN true ELSE false END as enabled FROM account_info WHERE email=?");
        //defne query to the authorities/roles by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select a.email, m.type_value from account_info a join master_data m on a.role_id = m.type_key\n" +
                "where type_name='ROLE' and a.email=?");
        return jdbcUserDetailsManager;
    }

//
    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(configurer ->
//                        configurer
//                                .requestMatchers("/", "/home","/register","/forgot-password",
//                                        "/reset-password", "/about", "/contact","/user_side/**","register/**").permitAll()
//                                .requestMatchers("/parent/**").hasAuthority("Parent")
//                                .requestMatchers("/school-owner/**").hasAnyAuthority("School owner", "Admin")
//                                .requestMatchers("/admin/**").hasAuthority("Admin")
//                                .anyRequest().authenticated()
//                )
//                .formLogin(form ->
//                        form
//                                .loginPage("/showMyLoginPage")
//                                .loginProcessingUrl("/authenticateTheUser")
//                                .successHandler(successHandler)
//                                .permitAll())
//                .logout(logout -> logout.permitAll()
//                )
//                .exceptionHandling(configurer -> configurer.accessDeniedPage("/access-denied"))
//                .sessionManagement(session -> session
//                        .sessionFixation().migrateSession()
//                        .maximumSessions(1)
//                        .expiredUrl("/showMyLoginPage")
//                )
//                .rememberMe(rememberMe -> rememberMe
//                        .key("uniqueAndSecret")
//                        .tokenValiditySeconds(86400)
//                );
//        return http.build();
//    }
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/", "/user_side/**","/public/**").permitAll()
                                .requestMatchers("/user/**").not().hasAnyAuthority("School owner", "Admin")
                                .requestMatchers("/auth/**").hasAnyAuthority("Parent","School owner","Admin")
                                .requestMatchers("/parent/**").hasAuthority("Parent")
                                .requestMatchers("/school-owner/**").hasAuthority("School owner")
                                .requestMatchers("/manager/**").hasAnyAuthority("School owner", "Admin")
                                .requestMatchers("/admin/**").hasAuthority("Admin")
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/public/showMyLoginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .successHandler(successHandler)
                                .failureHandler(authenticationHandler)
                                .permitAll())
                .logout(logout -> logout.permitAll()
                )
                .exceptionHandling(configurer -> configurer.accessDeniedPage("/access-denied"));

        return http.build();
    }


}
