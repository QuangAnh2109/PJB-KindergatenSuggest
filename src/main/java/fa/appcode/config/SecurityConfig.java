package fa.appcode.config;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.QueryConstant;
import fa.appcode.web.controller.AuthenticationHandler;
import fa.appcode.web.controller.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
/**
 * Returns an Image object that can then be painted on the screen.
 * The url argument must specify an absolute <a href="#{@link}">{@link URL}</a>. The name
 * argument is a specifier that is relative to the url argument.
 * <p>
 * This method always returns immediately, whether or not the
 * image exists. When this applet attempts to draw the image on
 * the screen, the data will be loaded. The graphics primitives
 * that draw the image will incrementally paint on the screen.
 *
 * @param  url  an absolute URL giving the base location of the image
 * @param  name the location of the image, relative to the url argument
 * @return      the image at the specified URL
 * @see         Image
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationSuccessHandler successHandler;
    private final AuthenticationHandler authenticationHandler;


    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        //define query to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(QueryConstant.USERS_BY_USERNAME);
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(QueryConstant.AUTHORITIES_BY_USERNAME);
        return jdbcUserDetailsManager;
    }

    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/general/**").anonymous()
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
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID").permitAll()
                )
                .exceptionHandling(configurer -> configurer.accessDeniedPage("/public/access-denied"));

        return http.build();
    }


}
