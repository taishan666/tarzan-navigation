package com.tarzan.nacigation.common.config;

import com.tarzan.nacigation.common.handler.CommonDataHandler;
import com.tarzan.nacigation.common.props.StaticHtmlProperties;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tarzan liu
 * @date 2020/4/18 11:58 上午
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties({StaticHtmlProperties.class})
public class WebMvcConfig implements WebMvcConfigurer {

    private final StaticHtmlProperties staticHtmlProperties;
    private final CommonDataHandler commonDataInterceptor;

    /**
     * 配置本地文件上传的虚拟路径和静态化的文件生成路径
     * 备注：这是一种图片上传访问图片的方法，实际上也可以使用nginx反向代理访问图片
     *
     * @param registry 注册表
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态化
        String staticFolder = staticHtmlProperties.getFolder();
        staticFolder = StringUtils.appendIfMissing(staticFolder, File.separator);
        registry.addResourceHandler(staticHtmlProperties.getAccessPathPattern())
                .addResourceLocations("file:" + staticFolder);
        //后管静态资源映射
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/");
        //网站图标
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/assets/favicon.ico");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonDataInterceptor).addPathPatterns("/**");
    }


    @Bean
    public ClassLoaderTemplateResolver localTemplateResolver() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setOrder(0);
        resolver.setCheckExistence(true);
        return resolver;
    }




}
