package com.awo.sample.common.spi;

import java.lang.annotation.*;

/**
 * @author: Create by awo
 * @date: 2020/4/19
 * @Discription: 定义在扩展类的接口上,表明可以被 ExtensionLoader加载
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

    String value() default "";
}
