package fa.appcode.config;

import fa.appcode.common.utils.Constant;
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
                "SELECT email, password, CASE WHEN status_id = 1 THEN true ELSE false END as enabled FROM account_info WHERE email=?");
        //defne query to the authorities/roles by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select a.email, m.type_value from account_info a join master_data m on a.role_id = m.type_key\n" +
                "where type_name='ROLE' and a.email=?");
        return jdbcUserDetailsManager;
    }

    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/", "/user_side/**", "/public/**").permitAll()
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
                                .successHandler(successHandler)
                                .failureHandler(authenticationHandler)
                                .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/public/home")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .exceptionHandling(configurer -> configurer.accessDeniedPage("/public/access-denied"));

        return http.build();
    }


}
