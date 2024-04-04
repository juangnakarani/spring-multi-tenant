package info.juangnakarani.springmultitenant.config;

import info.juangnakarani.springmultitenant.interceptor.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor();

    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }
}
