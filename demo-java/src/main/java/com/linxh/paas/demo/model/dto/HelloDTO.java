package com.linxh.paas.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author lin
 */
@Data
public class HelloDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 4977416797410990006L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "名称")
    @Max(value = 20, message = "名称长度不能超过20")
    private String name;

    // @ApiModelProperty(value = "所有老师")
    @ApiParam(value = "所有老师", hidden = true)
    private List<String> clazzes;

    @ApiModelProperty(value = "所有老师类型")
    private List<Type> clazzeTypes;

    @ApiModelProperty(value = "所有老师类型隐藏")
    @ApiParam(hidden = true)
    private List<Type> clazzeTypeHides;

    @ApiModelProperty(value = "类型")
    private Type type;

    @ApiModelProperty(value = "创建时间:结束[yyyy-MM-dd HH:mm:ss]", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime1111;


    @Data
    static class Type {

        @ApiModelProperty(value = "类型")
        private String t;
    }

}
