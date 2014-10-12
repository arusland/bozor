package com.arusland.bozor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by ruslan on 29.09.2014.
 */
@Configuration
public class ApplicationConfig extends WebMvcConfigurerAdapter {

    @Bean
    public MessageSource messageSource() {

        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages/messages", "classpath:messages/validation");
        messageSource.setUseCodeAsDefaultMessage(false);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(0);

        return messageSource;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jva = new HibernateJpaVendorAdapter();
        jva.setShowSql(true);
        jva.setGenerateDdl(true);
        jva.setDatabase(Database.MYSQL);

        return jva;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Configuration
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {
        @Value("${users.admin.password}")
        private String adminPassword;

        @Autowired
        private SecurityProperties security;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/template/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .anyRequest().fullyAuthenticated().and()
                    .logout()
                    .logoutSuccessUrl("/")
                    .logoutUrl("/logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).and()
                .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login?error").permitAll().and()
                .csrf().disable();
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    .withUser("admin").password(adminPassword)
                    .roles("ADMIN", "USER");
        }
    }
}
