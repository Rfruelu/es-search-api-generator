package com.es.search.api.generator;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.json.JsonData;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 通用转换
 *
 * @author LuTshoes
 * @version 1.0
 */
public class GeneralConvertHandler implements IConvertHandler {

    /**
     * 将注解和对象转换为BoolQuery
     *
     * @param annotation 注解
     * @param o          对象
     * @return 转换后的BoolQuery
     */
    @Override
    public BoolQuery convert(Annotation annotation, Object o) {
        // 判断注解是否为GeneralConvert类型并且对象不为空
        if (annotation instanceof GeneralConvert && Objects.nonNull(o)) {
            // 获取注解的key值
            String key = ((GeneralConvert) annotation).key();
            // 获取注解的查询模式
            EsQueryMode mode = ((GeneralConvert) annotation).mode();

            // 使用switch语句根据查询模式执行不同的逻辑
            switch (mode) {
                case TERM:
                    // 如果查询模式是TERM，则构建BoolQuery对象，添加term查询条件
                    return QueryBuilders.bool().must(t -> t.term(f -> f.field(key).value(FieldValue.of(JsonData.of(o))))).build();
                case TERMS:
                    // 如果查询模式是TERMS，并且对象是集合类型
                    if (o instanceof Collection) {
                        // 将对象转换为集合
                        Collection<?> collection = (Collection<?>) o;
                        // 将集合中的每个元素转换为FieldValue对象，并构建成列表
                        List<FieldValue> fieldValues = collection.stream().map(c -> FieldValue.of(JsonData.of(c))).collect(Collectors.toList());
                        // 构建BoolQuery对象，添加terms查询条件
                        return QueryBuilders.bool().must(t -> t.terms(f -> f.field(key).terms(v -> v.value(fieldValues)))).build();
                    }
                    break;
                case WILDCARD:
                    // 如果查询模式是WILDCARD，则构建BoolQuery对象，添加wildcard查询条件
                    return QueryBuilders.bool().must(t -> t.wildcard(f -> f.field(key).value("*" + o + "*"))).build();
                case RANGE:
                    // 如果查询模式是RANGE，并且对象是EsRangeObject类型
                    if (o instanceof EsRangeObject) {
                        // 将对象转换为EsRangeObject类型
                        EsRangeObject rangeObject = (EsRangeObject) o;
                        // 创建RangeQuery.Builder对象，设置查询的字段
                        RangeQuery.Builder range = QueryBuilders.range().field(key);
                        // 如果EsRangeObject的from属性不为空，则添加gte查询条件
                        Optional.ofNullable(rangeObject.getFrom()).ifPresent(from -> range.gte(JsonData.of(from)));
                        // 如果EsRangeObject的to属性不为空，则添加lte查询条件
                        Optional.ofNullable(rangeObject.getTo()).ifPresent(to -> range.lte(JsonData.of(to)));

                        // 构建BoolQuery对象，添加range查询条件
                        return QueryBuilders.bool().must(range.build()._toQuery()).build();
                    }
                    break;
                default:
                    // 如果查询模式不匹配任何已知模式，则不执行任何操作
                    break;
            }
        }
        // 如果注解不是GeneralConvert类型或者对象为空，则返回null
        return null;
    }

}
