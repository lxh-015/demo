package com.linxh.paas.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import springfox.documentation.annotations.ApiIgnore;

import javax.management.Query;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 基础 DTO
 *
 * @author lin
 */
@Data
public class BaseDTO {

    @ApiModelProperty(value = "创建时间:开始[yyyy-MM-dd HH:mm:ss]")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String startTime;

    @ApiModelProperty(value = "创建时间:结束[yyyy-MM-dd HH:mm:ss]")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String finishTime;

    @ApiModelProperty(value = "createTime", hidden = true)
    private List<Timestamp> createTime;

    /**
     * 时间转换, 当开始时间和结束时间都存在时，转换为BETWEEN查询
     */
    public void timeConvert() {
        if (this.startTime != null && this.finishTime != null) {
            createTime = Stream.of(Timestamp.valueOf(startTime).getTime(), Timestamp.valueOf(finishTime).getTime()).map(Timestamp::new).collect(Collectors.toList());
            startTime = finishTime = null;
        }
    }

}
