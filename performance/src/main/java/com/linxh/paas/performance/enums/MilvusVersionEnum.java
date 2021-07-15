package com.linxh.paas.performance.enums;

import lombok.Getter;

/**
 * @author lin
 */
@Getter
public enum MilvusVersionEnum {
    /**
     * milvus 服务器信息
     */
    V1_0_0("192.168.2.170", 19585),
    V1_1_0("10.10.8.218", 19555),
    V1_1_0_DIRECT_RW("192.168.2.89", 19531),
    V1_1_0_DIRECT_RO("192.168.2.89", 19532),
    ;


    MilvusVersionEnum(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    private String host;
    private Integer port;
}