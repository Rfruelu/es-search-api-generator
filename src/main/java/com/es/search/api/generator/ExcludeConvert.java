package com.es.search.api.generator;

import java.lang.annotation.*;

/**
 * 排除字段转换
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface ExcludeConvert {

}
