package com.zs.config;

import com.zs.handler.AdminLoginInterceptor;
import com.zs.handler.UserLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.List;

/**
 * web配置类
 * @Created by zs on 2022/2/22.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /* 登录拦截器 */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/admin/loginPage", "/admin/login", "/admin/login.do",
                                     "/admin/loginOut", "/admin/validate", "/admin/register.do",
                                     "/res/**", "/error","/blog/**","/main/**", "/v2/**");
        registry.addInterceptor(new UserLoginInterceptor())
                .addPathPatterns("/v2/**")
                .excludePathPatterns("/v2/index/**")
                .excludePathPatterns("/v2/article/view/**")
                .excludePathPatterns("/v2/lr/login.do")
                .excludePathPatterns("/v2/lr/register.do")
                .excludePathPatterns("/v2/lr/code")
                .excludePathPatterns("/v2/lr/check")
                .excludePathPatterns("/v2/lr/access/**")
                .excludePathPatterns("/v2/thumbs-up/get/*")
                .excludePathPatterns("/v2/my-alipay/pay")
                .excludePathPatterns("/v2/my-alipay/pay-notify");
    }

    /* 实时显示上传后的文件，而不需重启服务器 */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/res/images/category/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + Const.CATEGORY_PICTURE_SAVE_DIR);
        registry.addResourceHandler("/res/images/blogPicture/**")
                .addResourceLocations("file:"+System.getProperty("user.dir") + Const.BLOG_FIRST_PICTURE_SAVE_DIR);
    }

   /* 定义一个单例的map由IoC管理。用于处理editormd上传图片请求时，保存上传用户的ip，图片路径 */
    @Bean("imageMap")
    public HashMap<String,List<String>> imageMap() {
        return new HashMap<String,List<String>>();
    }

    /* 定义一个单例的map，用于管理用户注册时生成的验证码,key:邮箱地址 value:验证码 */
    // FIXME 后续可使用redis替换
    @Bean("validateCodeMap")
    public HashMap<String, String>  validateCodeMap() {
        return new HashMap<>();
    };
}
