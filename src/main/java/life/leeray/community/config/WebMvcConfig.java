package life.leeray.community.config;

import life.leeray.community.intercepter.SessionIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/26 0026 16:10
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    SessionIntercepter sessionIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionIntercepter).addPathPatterns("/**");
    }
}
