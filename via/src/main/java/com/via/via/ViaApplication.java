package com.via.via;

import com.via.security.AjaxInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.resource.ContentVersionStrategy;
import org.springframework.web.servlet.resource.VersionResourceResolver;

import java.util.Locale;


@SpringBootApplication
@EntityScan("com.via.entity")
@ComponentScan(basePackages = {"com.via", "com.web"})
@EnableScheduling
public class ViaApplication extends SpringBootServletInitializer {

    @Value("${file.basePath}")
    private String imageBasePath;

    @Value("${excel.basePath}")
    private String excelTemplatePath;

    @Value("${resourceVersion}")
    private String resourceVersion;
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ViaApplication.class);
    }


    public static void main(String[] args) {
        SpringApplication.run(ViaApplication.class, args);
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver slr = new CookieLocaleResolver();
        slr.setDefaultLocale(Locale.ENGLISH);
        slr.setCookieMaxAge(3600);
        slr.setCookieName("Language");
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
               // registry.addInterceptor(new LocaleChangeInterceptor()).addPathPatterns("/**");
                registry.addInterceptor(new AjaxInterceptor()).addPathPatterns("/form/addRow");
                registry.addInterceptor(localeChangeInterceptor());

            }
            
        };
    }


}
