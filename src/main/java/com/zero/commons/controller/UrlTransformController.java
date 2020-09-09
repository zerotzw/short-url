package com.zero.commons.controller;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zero.commons.utils.HexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description
 * @Author tongzw
 * @Date 2020-09-08 16:08
 */
@Controller
public class UrlTransformController {

    private Logger logger = LoggerFactory.getLogger(UrlTransformController.class);

    private static AtomicInteger sqNum = new AtomicInteger(10000);

    final static Cache<String, String> cacheUrl = CacheBuilder.newBuilder()
            //设置cache的初始大小为10，要合理设置该值
            .initialCapacity(100)
            //设置并发数为5，即同一时间最多只能有5个线程往cache执行写入操作
            .concurrencyLevel(100)
            //设置cache中的数据在写入之后的存活时间为10秒
            .expireAfterWrite(100, TimeUnit.SECONDS)
            //构建cache实例
            .build();

    final static Cache<String, String> cachesign = CacheBuilder.newBuilder()
            //设置cache的初始大小为10，要合理设置该值
            .initialCapacity(100)
            //设置并发数为5，即同一时间最多只能有5个线程往cache执行写入操作
            .concurrencyLevel(100)
            //设置cache中的数据在写入之后的存活时间为10秒
            .expireAfterWrite(10000, TimeUnit.SECONDS)
            //构建cache实例
            .build();


    @GetMapping( "short")
    @ResponseBody
    public Object transformShortUrl(@RequestParam("sign") String sign,@RequestParam("timeout")Long timeout)throws  Exception {

        logger.info(sign);
        String url;
        if (null == cachesign.getIfPresent(sign)) {
            url = shortUrl();
            cachesign.put(sign,url);
            cacheUrl.put(url,sign);
        }else{

            url = cachesign.getIfPresent(sign);
        }
        return url;
    }


    @RequestMapping("red/{type}")
    public Object redirect(@PathVariable String type, HttpServletRequest httpServerRequest)throws  Exception {



        String url = httpServerRequest.getRequestURL().toString();
        if (null == cacheUrl.getIfPresent(url)) {

            return "不存在的地址";
        }else{

            return "redirect:" + cacheUrl.getIfPresent(url);
        }
    }


    private static String shortUrl() {

        return "http://tzw/red/" + HexUtil._10_to_62(sqNum.addAndGet(1),4);
    }



}
