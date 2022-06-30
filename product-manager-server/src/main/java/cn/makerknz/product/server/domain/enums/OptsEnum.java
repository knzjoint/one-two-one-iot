package cn.makerknz.product.server.domain.enums;

import lombok.Getter;

@Getter
public enum OptsEnum {
    Qos0(0),
    Qos1(1),
    Qos2(2)
    ;

    Integer qos;

    OptsEnum(Integer qos) {
        this.qos = qos;
    }
}
