package com.es.search.api.generator;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;

/**
 * 抽象的es搜索条件对象
 * <p>
 * 当使用一个值对象作为ES索引搜索条件入参对象时，该值对象的定义本身就包含了ES索引搜索条件的处理逻辑
 * 而ES搜索条件对象（BoolQueryBuilder）的构建应该由条件入参对象对外提供转换构建方法
 *
 * @author LuTshoes
 * @version 1.0
 */
public abstract class AbstractEsConditionReqDto {

    public abstract BoolQuery build() throws IllegalAccessException;



}

