package cn.makerknz.product.server.rule.core.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/5/3 18:03
 */

@Data

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "linkType",
        visible = true)

@JsonSubTypes({
        @JsonSubTypes.Type(value = ThingData.class, name = "THINGS_TYPE"),
//        @JsonSubTypes.Type(value = MultipleChoiceQuestion.class, name = Question.MULTIPLE_CHOICE),
//        @JsonSubTypes.Type(value = TrueOrFalseQuestion.class, name = Question.TRUE_OR_FALSE),
})
public class DataBase {

    private Integer id;

    /**
     * 数据类型,用来反序列化使用
     */
    private String linkType;

}
