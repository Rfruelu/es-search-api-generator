package com.es.search.api.generator;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author LuTshoes
 * @Description: ES 范围对象
 * @Version: 1.0
 */
@Data
@Accessors(chain = true)
public class EsRangeObject {


    /**
     * 下限
     */
    private Object from;
    /**
     * 上限
     */
    private Object to;
}
