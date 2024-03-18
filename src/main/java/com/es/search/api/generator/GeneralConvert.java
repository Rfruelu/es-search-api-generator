package com.es.search.api.generator;

import java.lang.annotation.*;

/**
 * @description:       通用转换
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface GeneralConvert {

    /**
     * 获取键值
     *
     * @return 返回键值
     */
    String key();

    /**
     * 获取当前ES查询模式
     *
     * @return 返回当前ES查询模式
     */
    EsQueryMode mode();

}
