package at.qe.skeleton.configs;

import javax.sql.DataSource;

import at.qe.skeleton.model.UserRole;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


/**
 * Spring configuration for web security.
 * This class is part of the skeleton project provided for students of the
 * course "Software Engineering" offered by the University of Innsbruck.
 */
@Configuration
@EnableWebSecurity()
public class WebSecurityConfig {

    private static final String ADMIN = UserRole.ADMIN.name();
    private static final String GARDENER = UserRole.GARDENER.name();
    private static final String USER = UserRole.USER.name();

    @Autowired
    DataSource dataSource;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, LoginSuccessHandler loginSuccessHandler) {
        try {
            http.csrf().disable();
            http.headers().frameOptions().disable(); // needed for H2 console

            http.logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID");

            http.authorizeHttpRequests()
                    //.requestMatchers(antMatcher("/h2-console/**")).permitAll()
                    .requestMatchers("/pics/**").permitAll()
                    .requestMatchers("/gallery/upload-dir/**").permitAll()
                    .requestMatchers("/besucher/**").permitAll()
                    .requestMatchers("/admin/**").hasAnyAuthority(ADMIN)
                    .requestMatchers("/gartner/**").hasAnyAuthority(GARDENER)
                    .requestMatchers("/user/**").hasAnyAuthority(USER)
                    //Permit access only for some roles
                    .anyRequest().authenticated()
                    .and().formLogin()
                    .successHandler(loginSuccessHandler)
                    .and().httpBasic();

            return http.build();
        }
        catch (Exception ex) {
            throw new BeanCreationException("Wrong spring security configuration", ex);
        }
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //Configure roles and passwords via datasource
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from userx where username=?")
                .authoritiesByUsernameQuery("select userx_username, roles from userx_user_role where userx_username=?")
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}