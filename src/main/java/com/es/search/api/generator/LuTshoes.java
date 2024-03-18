package com.es.search.api.generator;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * test class
 *
 * @author : LuTshoes
 **/
@Data
@Accessors(chain = true)
public class LuTshoes extends  AbstractEsConditionReqDto{


    @GeneralConvert(key = "term", mode = EsQueryMode.TERM)
    private String term;

    @GeneralConvert(key = "terms", mode = EsQueryMode.TERMS)
    private List<String> terms;

    @GeneralConvert(key = "wildcard", mode = EsQueryMode.WILDCARD)
    private String wildcard;
    @GeneralConvert(key = "rangeObject", mode = EsQueryMode.RANGE)
    private EsRangeObject rangeObject;

    public static void main(String[] args) throws IllegalAccessException {


        LuTshoes luTshoes = new LuTshoes().setTerm("term").setRangeObject(new
                EsRangeObject().setFrom("100").setTo("200")).setWildcard("123456").setTerms(List.of("terms","2"));

        System.out.println(luTshoes.build());
    }

    @Override
    public BoolQuery build() throws IllegalAccessException {

        // 也可以自己定义实现
        return BoolQueryAdapter.convert(this);
    }
}