package com.via.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security Configuration Class
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LoggingAccessDeniedHandler loggingAccessDeniedHandler;

    @Autowired
    private SecurityUserDetailService securityUserDetailService;

    @Value("${file.basePath}")
    private String imageBasePath;

    @Value("${excel.basePath}")
    private String excelTemplatePath;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        /**Using inMemory Auth for fast demo*/
        auth.userDetailsService(securityUserDetailService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/form", "/form/**", "/fragments", "/api/**");
    }


    @Override
    protected void configure(final HttpSecurity http) throws Exception {


        http
            .headers().disable()
            .authorizeRequests()
            .antMatchers(
                    "/",
                    "/static/css/**",
                    "/static/img/**",
                    "/static/js/**",
                    "/webjars/**",
                    "/form",
                    "/registration",
                    "/register",
                    "/form/**",
                    "/images/**",
                    "/fragment").permitAll()
            .antMatchers("/user/**", "/form-list/**", "/images/**", imageBasePath, excelTemplatePath)
            .hasRole("USER")
            //.antMatchers("/api/**").authenticated()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/form-list/form-list", true)
            .permitAll()
            .and()
            .logout()
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login?logout")
            .permitAll()
            .and()
            .csrf().disable()
            .exceptionHandling()
            .accessDeniedHandler(loggingAccessDeniedHandler);


    }



}
