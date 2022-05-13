package com.zs.aspect;

import com.zs.config.Const;
import com.zs.handler.UniversalException;
import com.zs.pojo.Blog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 切面类
 *   删除博客提交、保存后未使用，但已上传到服务器的图片文件
 * @Created by zs on 2022/3/21.
 */
@Component
@Aspect
public class ImagesAspect {

    @Resource
    private HashMap<String, List<String>> imageMap;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * 切点：发布 / 编辑博客请求
     */
    @Pointcut("execution(* com.zs.controller.BlogController.doEdit(..))")
    public void image() {}

    /**
     * 后置通知
     *   处理博客发布 / 保存后的未使用图片,避免无意义的占用空间
     *   TODO 用户在editormd中编辑时删除的图片
     * @param joinPoint
     */
    @After("image()")
    public void doBlogAfter(JoinPoint joinPoint) {
        // 获取request对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        // 获取所有请求参数，第一个为接收的blog对象
        Object blogAttr = joinPoint.getArgs()[0];
        // 获取请求ip
        String remoteAddr = request.getRemoteAddr();
        if (blogAttr instanceof Blog) {
            Blog blog = (Blog) blogAttr;
            // 获取当前请求ip上传的图片路径列表
            List<String> listDir = imageMap.get(remoteAddr);
            if (listDir == null || listDir.size() == 0) {
                imageMap.remove(remoteAddr);
                LOGGER.info("doEdit请求后置通知-->图片处理条数: 0 时间: {}",new Date());
                return;
            } else {
                // 删除未使用的图片文件
                String content = blog.getContent();
                Stream<String> discardStream = listDir.stream().filter(dir -> content.contains(dir) == false);
                List<String> discardListDir = discardStream.collect(Collectors.toList());
                if (discardListDir.size() != 0) {
                    Iterator<String> discardIterator = discardListDir.iterator();
                    while (discardIterator.hasNext()) {
                        String dir = discardIterator.next();
                        File file = new File(System.getProperty("user.dir") + Const.BLOG_FIRST_PICTURE_SAVE_DIR + dir);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    LOGGER.info("doEdit请求后置通知执行-->图片处理条数: {} 时间: {}", discardListDir.size(), new Date());
                    // 图片处理完后将imageMap集合对应的键值对清除
                    imageMap.remove(remoteAddr);
                    return;
                }
                imageMap.remove(remoteAddr);
                LOGGER.info("doEdit请求后置通知执行-->图片处理条数: {} 时间: {}", discardListDir.size(), new Date());
                return;
            }
        } else {
            imageMap.remove(remoteAddr);
            LOGGER.info("doEdit请求后置通知执行-->参数类型异常 时间:{}", new Date());
            throw new UniversalException("doEdit请求后置通知执行-->参数类型异常");
        }
    }

}
