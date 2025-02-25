package fa.appcode.config;
import fa.appcode.web.controller.CustomAuthenticationSuccessHandler;
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

    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        //define query to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery("select email, password, status_id  from account_info where email=?");
        //defne query to the authorities/roles by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select a.email, m.type_value from account_info a join master_data m on a.role_id = m.type_key\n" +
                "where type_name='ROLE' and a.email=?");
        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer->
                        configurer
                                .requestMatchers("/", "/home","/register","/forgot-password",
                                        "/reset-password", "/about", "/contact","/user_side/**","register/verify").permitAll()
                                .requestMatchers("/parent/**").hasAuthority("Parent")
                                .requestMatchers("/school-owner/**").hasAnyAuthority("School owner", "Admin")
                                .requestMatchers("/admin/**").hasAuthority("Admin")
                                .anyRequest().authenticated()
                )
                .formLogin(form->
                        form
                                .loginPage("/showMyLoginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .successHandler(successHandler)
                        .permitAll())
                .logout(logout->logout.permitAll()
                )
                .exceptionHandling(configurer -> configurer.accessDeniedPage("/access-denied"));
        return http.build();
    }
}
