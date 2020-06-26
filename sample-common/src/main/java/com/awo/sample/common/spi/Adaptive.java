package com.awo.sample.common.spi;

import java.lang.annotation.*;

/**
 * @author: Create by awo
 * @date: 2020/6/7
 * @Discription:
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Adaptive {

    String[] group() default {};

    String[] value() default {};
}
