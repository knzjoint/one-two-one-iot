package cn.makerknz.product.server.sms;

import cn.makerknz.product.server.domain.enums.SmsTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/6/30 13:15
 */

@Component
@Slf4j
@ConditionalOnMissingBean(AliSmsCodeSend.class)
public class CommonSmsCode implements ISmsCodeSend {

    @Override
    public void send(String phoneNo, String code, SmsTypeEnum smsType) {
        log.info("手机{}的验证码为：code = {}", phoneNo, code);
    }

}
