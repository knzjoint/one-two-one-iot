package cn.makerknz.product.server.sms;

import cn.makerknz.product.server.domain.enums.SmsTypeEnum;

/**
 * @Author: 朱康南
 * @Date: 2019/9/4/004 19:09
 * @Version 1.0
 */

public interface ISmsCodeSend {

    void send(String phoneNo, String code, SmsTypeEnum smsType);

}