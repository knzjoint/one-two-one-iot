package cn.makerknz.product.server.sms;

import cn.makerknz.product.server.config.SmsConfig;
import cn.makerknz.product.server.domain.enums.SmsTypeEnum;
import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置阿里云消息服务，并发送验证码
 * @Author: maker_knz
 * @Date: 2019/9/4/004 19:11
 * @Version 1.0
 */

@Component
@Slf4j
//@ConditionalOnExpression("T(org.apache.commons.lang3.StringUtils).isNotEmpty('${one-two-one.sms.ali-yun-sms}')")
//@ConditionalOnExpression("${one-two-one.sms.ali-yun-sms.enabled:true}")
@ConditionalOnProperty(
        value = {"one-two-one.sms.ali-yun-sms"},
        matchIfMissing = false
)
public class AliSmsCodeSend implements ISmsCodeSend {

    @Autowired
    private SmsConfig smsConfig;

    @Autowired
    private Client aliyunSmsCode;

    @Override
    public void send(String mobile, String code, SmsTypeEnum smsType) {
        log.info("手机{}的验证码为：code = {}", mobile, code);
        try {
            if (smsConfig.getEnabled()) {
                this.aliSmsSender(mobile, code, smsType);
            }
        } catch (Exception e){
            throw new BusinessException(ExceptionEnum.USER_ALIYUN_SMS_ERROR);
        }
    }

    private void aliSmsSender(String mobile, String code, SmsTypeEnum smsType) throws Exception {
        Map reqMap = new HashMap<String, String>();

        reqMap.put("PhoneNumbers", mobile);
        reqMap.put("SignName", smsConfig.getSignName());
        reqMap.put("TemplateParam", String.format(smsConfig.getCodeFormat(), code));
        reqMap.put("TemplateCode", smsType.getTemplate());
//        reqMap.put("RegionId", smsConfig.getRegionId());

        SendSmsRequest sendSmsRequest = SendSmsRequest.build(reqMap);
        SendSmsResponse resp = aliyunSmsCode.sendSms(sendSmsRequest);
        log.info(resp.getBody().toString());
    }
}
