package cn.makerknz.product.server.utils;

import cn.makerknz.product.server.domain.enums.SmsTypeEnum;
import cn.makerknz.product.server.sms.ISmsCodeSend;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @Author: maker_knz
 * @Date: 2021/7/12/012 10:14
 * @Version 1.0
 */

@Slf4j
@Component
public class SmsUtils {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private ISmsCodeSend smsCodeSend;

    private static final Integer AVAILABLE_TIME = 5;

    private static final Integer CODE_LENGTH = 4;

    private final Integer SMS_LIMIT_COUNT = 3;

    public void send(String phoneNo, SmsTypeEnum smsType) {
        // 判断验证码是否存在
        Object value = redisTemplate.opsForValue().get(this.buildSmsCodeKey(phoneNo, smsType));
        if (value != null) {
            throw new RuntimeException("验证码发送中");
        }

        // 1. 生成验证码
        String code = this.generate();

        // 2.发送code
        smsCodeSend.send(phoneNo, code, smsType);
        log.info("手机{}的验证码为：code = {}", phoneNo, code);

        // 3.将code存储, 并设置发送的次数
        this.save(phoneNo, code, smsType);
        this.sendCountLimit(phoneNo, smsType);
    }


    /**
     * 验证验证码
     *
     * @param phoneNo
     * @param code
     * @param smsTypeEnum
     * @return SmsResponse
     */
    public boolean validateCode(String phoneNo, String code, SmsTypeEnum smsTypeEnum) {
        // 1、判断上次注册码是否已经失效
        String codeNum = this.get(phoneNo, smsTypeEnum);
        if (codeNum == null) {
            log.warn("[验证验证码], 验证码失效");
            throw new RuntimeException("验证码失效");
        }

        // 2、判断code是否相等
        if (!code.equals(codeNum)) {
            log.warn("[验证验证码], 验证码错误,验证码={},存储的验证码={},请求类型={}", code, codeNum, smsTypeEnum);
            throw new RuntimeException("验证码错误");
        }

        // 3、验证成功,将验证码移除
        this.remove(phoneNo, smsTypeEnum);

        // 4、移除对次数的限制
        String limitKey = this.buildSmsLimitKey(phoneNo, smsTypeEnum);
        redisTemplate.delete(limitKey);

        return true;
    }

    /**
     * 限制验证码发送的次数
     * @param phoneNo
     * @param smsTypeEnum
     */
    private void sendCountLimit(String phoneNo, SmsTypeEnum smsTypeEnum) {
        String limitKey = this.buildSmsLimitKey(phoneNo, smsTypeEnum);
        long count = redisTemplate.opsForValue().increment(limitKey, 1);
        if (count > SMS_LIMIT_COUNT) {
            log.warn("[发送验证],验证码发送太频繁");
            throw new RuntimeException("验证码发送太频繁");
        } else {
            // 验证码次数凌晨清除 // TODO 可以设置为两个小时
            Duration duration = Duration.between(LocalDateTime.now(), LocalDate.now().plusDays(1).atTime(0, 0, 0));
            redisTemplate.expire(limitKey, duration.toMinutes(), TimeUnit.MINUTES);
        }
    }

    public String generate() {
        String code = RandomStringUtils.randomNumeric(CODE_LENGTH);
        return code;
    }

    /**
     * 保存验证码
     *
     * @param code
     */
    public void save(String phoneNo, String code, SmsTypeEnum smsTypeEnum) {

        String codeKey = this.buildSmsCodeKey(phoneNo, smsTypeEnum);

        // 让redis中存储配置分钟
        redisTemplate.opsForValue().set(codeKey,
                code, AVAILABLE_TIME, TimeUnit.MINUTES);
    }

    /**
     * 获取验证码
     *
     * @return
     */
    public String get(String phoneNo, SmsTypeEnum smsTypeEnum) {
        Object value = redisTemplate.opsForValue().get(this.buildSmsCodeKey(phoneNo, smsTypeEnum));
        if (value == null) {
            return null;
        }
        return (String) value;
    }

    /**
     * 移除验证码
     *
     * @param phoneNo
     */
    public void remove(String phoneNo, SmsTypeEnum smsTypeEnum) {
        redisTemplate.delete(this.buildSmsCodeKey(phoneNo, smsTypeEnum));
    }

    /**
     * 创建SMS code 的 KEY
     * @param phoneNo
     * @param smsTypeEnum
     * @return
     */
    public String buildSmsCodeKey(String phoneNo, SmsTypeEnum smsTypeEnum) {
        return "SMS_CODE:" + phoneNo.toLowerCase() + ":" + smsTypeEnum.getValue();
    }

    /**
     * 创建限制发送Code的次数KEY
     * @param phoneNo
     * @param smsTypeEnum
     * @return
     */
    public String buildSmsLimitKey(String phoneNo, SmsTypeEnum smsTypeEnum) {
        return "SMS_LIMIT:" + phoneNo.toLowerCase() + ":" + smsTypeEnum.getValue();
    }

}
