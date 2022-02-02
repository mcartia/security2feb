package it.training.spring.security;

import org.hibernate.bytecode.enhance.internal.tracker.NoopCollectionTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    public void configure(HttpSecurity security) throws Exception {
        security
                .authorizeRequests()
                .antMatchers("/api/public/**").permitAll()
                // Security settings per H2 Web console
                .antMatchers("/h2-console/**")
                .permitAll()
                .and().csrf().disable()
                .headers().frameOptions().disable()
                // -----------------------------------------
                .and().authorizeRequests()
                .antMatchers("/actuator/**")
                .hasRole("ADMIN")
                .antMatchers("/**")
                .hasRole("USER")
                .and().formLogin().permitAll()
                .and().httpBasic()
                .and().logout().permitAll();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder())
                .withUser("simpleuser")
                .password(passwordEncoder().encode("12345678"))
                .roles("USER").and()
                .withUser("admin")
                .password(passwordEncoder().encode("passw0rd"))
                .roles("USER","ADMIN");

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("SELECT USER_NAME, PASSWORD, ENABLED FROM CUSTOM_USER WHERE USER_NAME=?")
                .authoritiesByUsernameQuery("SELECT CUSTOM_USER_USER_NAME, AUTHORITIES FROM CUSTOM_USER_AUTHORITIES WHERE CUSTOM_USER_USER_NAME=?");
                /*.withDefaultSchema()
                .withUser("dbuser")
                .password(passwordEncoder().encode("12345678"))
                .roles("USER");*/
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
