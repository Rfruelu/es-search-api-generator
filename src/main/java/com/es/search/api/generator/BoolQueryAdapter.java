package com.es.search.api.generator;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;


/**
 * BoolQueryBuilder适配器
 * @author LuTshoes
 *
 */
public class BoolQueryAdapter {


    private static final Map<Class<? extends Annotation>, IConvertHandler> convertHandlerMap = new HashMap<>();

    static {
        convertHandlerMap.put(GeneralConvert.class, new GeneralConvertHandler());
    }

    /**
     * 将ES查询条件对象转换成QueryBuilder
     *
     * @param reqDto ES查询条件对象
     * @return QueryBuilder
     */
    public static BoolQuery convert(AbstractEsConditionReqDto reqDto) throws IllegalAccessException {

        // 创建一个BoolQueryBuilder对象作为根查询条件
        BoolQuery.Builder root = QueryBuilders.bool();

        // 如果查询条件对象为null，则直接返回空的BoolQueryBuilder
        if (Objects.isNull(reqDto)) {
            return root.build();
        }

        // 获取查询条件对象的所有字段
        List<Field> fieldList = Arrays.stream(reqDto.getClass().getDeclaredFields()).toList();

        // 遍历每个字段
        for (Field field : fieldList) {
            // 获取字段上的所有注解
            Annotation[] annotations = field.getAnnotations();

            // 如果该字段需要排除转换，则跳过该字段
            if (isExcludeConvert(annotations)) {
                continue;
            }

            // 查找第一个转换注解
            Annotation convertAnnotation = findFirstConvertAnnotation(annotations);

            // 如果找不到转换注解，则跳过该字段
            if (Objects.isNull(convertAnnotation)) {
                continue;
            }

            // 根据转换注解类型获取对应的转换处理器
            IConvertHandler convertHandler = convertHandlerMap.get(convertAnnotation.annotationType());

            // 如果找不到对应的转换处理器，则跳过该字段
            if (Objects.isNull(convertHandler)) {
                continue;
            }

            // 设置字段可访问（如果为私有字段）
            field.setAccessible(true);

            // 使用转换处理器将字段值转换成QueryBuilder
            BoolQuery queryBuilder = convertHandler.convert(convertAnnotation, field.get(reqDto));

            // 如果转换结果为null，则跳过该字段
            if (Objects.isNull(queryBuilder)) {
                continue;
            }

            // 将转换得到的QueryBuilder添加到根查询条件的must子句中
            root.must(queryBuilder._toQuery());
        }

        // 返回根查询条件的构建结果
        return root.build();
    }



    /**
     * 从给定的注解数组中查找第一个转换注解
     *
     * @param annotations 给定的注解数组
     * @return 第一个转换注解，如果找不到则返回null
     */
    private static Annotation findFirstConvertAnnotation(Annotation... annotations) {
        if (annotations == null || annotations.length == 0) {
            return null;
        }
        return Arrays.stream(annotations).filter(e -> convertHandlerMap.containsKey(e.annotationType())).findFirst().orElse(null);
    }

    /**
     * 判断是否排除转换
     *
     * @param annotations 注解数组
     * @return 如果存在 ExcludeConvert 注解，则返回 true，否则返回 false
     */
    private static boolean isExcludeConvert(Annotation... annotations) {
        if (annotations == null || annotations.length == 0) {
            return true;
        }
        return Arrays.stream(annotations).anyMatch(e -> ExcludeConvert.class == e.annotationType());
    }

}
