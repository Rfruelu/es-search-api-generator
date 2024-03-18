package com.es.search.api.generator;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;

import java.lang.annotation.Annotation;

/**
 *  @ClassName IConvertHandler
 * @author LuTshoes
 * @version 1.0
 */
public interface IConvertHandler {
    BoolQuery convert(Annotation annotation, Object o);
}
