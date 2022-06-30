# 连接EMQ X

## 简介

从EMQ X中获取消息的方式比较多

* 购买EMQ X的企业版，包括很多消息流转模块；

* 创建个EMQ X超级用户，订阅所有的消息事件；

* 使用规则引擎；

* 移植EMQ X的kafaka插件；

* 通过WebHook插件获取消息。

  ![image-20210728134815313](http://note.makerknz.cn/image-20210728134815313.png)

本文通过WebHook插件获取消息。设备的所有事件会通过webhook发送到产品服务器。

![image-20211121090519234](http://note.makerknz.cn/image-20211121090519234.png)

## [WebHook介绍](https://docs.emqx.cn/broker/v4.3/advanced/webhook.html)

WebHook 是由 [emqx_web_hook (opens new window)](https://github.com/emqx/emqx-web-hook)插件提供的 将 EMQ X 中的钩子事件通知到某个 Web 服务的功能。

可以理解为EMQ X创建了一个客户端，这个客户端可以收集设备的在线、下下线记录、订阅与消息存储、消息送达确认等事件消息，通过钩子上挂载回调函数将事件发送到web器。

```shell
    Client      |    EMQ X     |  emqx_web_hook |   HTTP       +------------+
  =============>| - - - - - - -> - - - - - - - ->===========>  | Web Server |
                |    Broker    |                |  Request     +------------+
```



```
WebHook消息是单向的。
```

### Webhook配置

### web.hook.url

| Type   | Value                                   |
| ------ | --------------------------------------- |
| string | http://192.168.31.216:9200/emqx/webhook |

Webhook 请求转发的目的 Web 服务器地址。



### web.hook.headers

```properties
web.hook.headers.content-type = application/json
web.hook.headers.accept = */*
web.hook.headers.webhook-username = makerknz
web.hook.headers.webhook-password = 123456
```

指定 HTTP 请求头部中的数据。`<Key>` 指定 HTTP 请求头部中的字段名，此配置项的值为相应的字段值。`<Key>` 可以是标准的 HTTP 请求头部字段，也可以自定义的字段，可以配置多个不同的请求头部字段。

webhook-username和webhook-password 作为访问后端的凭据，当emq x是一个集群时用来区别不同的服务器。当然认证也可以通过添加证书进行SSL认证，这部分感兴趣的同学可以自己探索。

## 触发规则

在 `etc/plugins/emqx_web_hooks.conf` 可配置触发规则，其配置的格式如下：

```bash
## 格式示例
web.hook.rule.<Event>.<Number> = <Rule>

## 示例值
web.hook.rule.message.publish.1 = {"action": "on_message_publish", "topic": "a/b/c"}
web.hook.rule.message.publish.2 = {"action": "on_message_publish", "topic": "foo/#"}
web.hook.rule.message.publish.2 = {"action": "on_message_publish"}
```

项目中会设置全部转发到后端服务器，不设置topic过滤。

### Event 触发事件

目前支持以下事件：

| 名称                 | 说明         | 执行时机                                       |
| -------------------- | ------------ | ---------------------------------------------- |
| client.connect       | 处理连接报文 | 服务端收到客户端的连接报文时                   |
| client.connack       | 下发连接应答 | 服务端准备下发连接应答报文时                   |
| client.connected     | 成功接入     | 客户端认证完成并成功接入系统后                 |
| client.disconnected  | 连接断开     | 客户端连接层在准备关闭时                       |
| client.subscribe     | 订阅主题     | 收到订阅报文后，执行 `client.check_acl` 鉴权前 |
| client.unsubscribe   | 取消订阅     | 收到取消订阅报文后                             |
| session.subscribed   | 会话订阅主题 | 完成订阅操作后                                 |
| session.unsubscribed | 会话取消订阅 | 完成取消订阅操作后                             |
| message.publish      | 消息发布     | 服务端在发布（路由）消息前                     |
| message.delivered    | 消息投递     | 消息准备投递到客户端前                         |
| message.acked        | 消息回执     | 服务端在收到客户端发回的消息 ACK 后            |
| message.dropped      | 消息丢弃     | 发布出的消息被丢弃后                           |

### Number

同一个事件可以配置多个触发规则，配置相同的事件应当依次递增。

### Rule

触发规则，其值为一个 JSON 字符串，其中可用的 Key 有：

- action：字符串，取固定值
- topic：字符串，表示一个主题过滤器，操作的主题只有与该主题匹配才能触发事件的转发

例如，我们只将与 `a/b/c` 和 `foo/#` 主题匹配的消息转发到 Web 服务器上，其配置应该为：

```bash
web.hook.rule.message.publish.1 = {"action": "on_message_publish", "topic": "a/b/c"}
web.hook.rule.message.publish.2 = {"action": "on_message_publish", "topic": "foo/#"}
```

这样 Webhook 仅会转发与 `a/b/c` 和 `foo/#` 主题匹配的消息，例如 `foo/bar` 等，而不是转发 `a/b/d` 或 `fo/bar`。

## WebFlux 介绍

WebFlux是Spring中的异步非阻塞的响应式web框架，EMQ X的设备有12种事件类型，对于单机安装的[EMQ X并发连接11万](https://blog.csdn.net/weixin_44455388/article/details/108714963)会产品webhook的请求，后端服务器是同步MVC会导致请求堆积，使服务处理逻辑。

如果通过web.hook.pool_size 配置连接池数，又会导致数据的延迟。

![img](http://5b0988e595225.cdn.sohucs.com/images/20200507/1a6c626d50df48b8a2109f790ec2b4dc.jpeg)

MVC JDBC是一个同步请求的过程，每一次请求都会在一个线程内执行，等一次请求完成之后才能释放资源，如果在service方法中业务逻辑如果碰到io操作时间比较长的操作，这样这个service方法就会长时间占用tomcat容器线程池中的线程，这样是不利于其他请求的处理的，当线程池中的线程处理任务时，任务由于长时间io操作，肯定会阻塞线程处理其他任务。

WebFlux R2DBC是一个异步非阻塞的请求过程，此处会涉及大量的EMQX的请求，如果请求堆积，可以通过设置背压调整每次处理的数量来达到对请求的削峰处理。

## ## WebHook接口设计

### 增加WebFlux接口

#### 添加依赖包

添加包WebFlux使用的包。r2dbc-mysql用来连接数据库。

```groovy
    implementation('org.springframework.boot:spring-boot-starter-webflux')
    implementation('org.springframework.boot:spring-boot-starter-data-r2dbc')
    implementation('dev.miku:r2dbc-mysql')
```

#### 配置

```yaml
spring:
  r2dbc:
    url: r2dbcs:mysql://www.makerknz.cn:3306/product_manager?characterEncoding=utf-8&useSSL=false
    username: product_manager
    password: 123456
```

#### 添加repository

WebHook目前设计只对外开放一个接口，仅用到device_events和device两张表，所以只写这两张表的repository即可。

DeviceEventsRepository

```java
package cn.makerknz.product.server.repository;

import cn.makerknz.product.server.entity.DeviceEvents;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface DeviceEventsRepository extends ReactiveCrudRepository<DeviceEvents, Integer> {
}
```

DeviceRepository

```
package cn.makerknz.product.server.repository;

import cn.makerknz.product.server.entity.Device;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface DeviceRepository extends ReactiveCrudRepository<Device, Integer> {

    Mono<Device> findByClientId(String clientId);

}
```

#### service实现

在DeviceEventsServiceImpl增加实现

```
package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.entity.DeviceEvents;
import cn.makerknz.product.server.mapper.DeviceEventsMapper;
import cn.makerknz.product.server.repository.DeviceEventsRepository;
import cn.makerknz.product.server.service.IDeviceEventsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author maker knz
 * @since 2021-10-27
 */

@Service
public class DeviceEventsServiceImpl extends ServiceImpl<DeviceEventsMapper, DeviceEvents> implements IDeviceEventsService {

    @Autowired
    private DeviceEventsRepository deviceEventsRepository;

    @Override
    public Mono<DeviceEvents> add(DeviceEvents deviceEvents) {
        return deviceEventsRepository.save(deviceEvents);
    }

}

```

在DeviceServiceImpl中增加实现

```
package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.entity.Device;
import cn.makerknz.product.server.mapper.DeviceMapper;
import cn.makerknz.product.server.repository.DeviceRepository;
import cn.makerknz.product.server.service.IDeviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author maker knz
 * @since 2021-10-27
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public Mono<Device> findByClientId(String clientId) {
        return deviceRepository.findByClientId(clientId);
    }
}

```

#### 对外接口

```java
package cn.makerknz.product.server.controller;

import cn.makerknz.product.server.annotation.CheckEmqxWebhookIdentity;
import cn.makerknz.product.server.domain.enums.ResponseEnum;
import cn.makerknz.product.server.domain.form.EmqxWebhookForm;
import cn.makerknz.product.server.domain.vo.ResultVO;
import cn.makerknz.product.server.entity.DeviceEvents;
import cn.makerknz.product.server.service.IDeviceEventsService;
import cn.makerknz.product.server.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/emqx")
public class EmqxController {

    @Autowired
    private IDeviceEventsService deviceEventsService;

    @Autowired
    private IDeviceService deviceService;

    @CheckEmqxWebhookIdentity
    @PostMapping("/webhook")
    public Mono<ResultVO<Object>> webhook(@RequestBody EmqxWebhookForm emqxWebhookForm) {

        return deviceService.findByClientId(emqxWebhookForm.getClientid()).flatMap(e -> {
            DeviceEvents deviceEvents = DeviceEvents.builder()
                    .productId(e.getProductId())
                    .data(emqxWebhookForm.toString())
                    .deviceId(e.getId())
                    .eventAction(emqxWebhookForm.getAction())
                    .streamId(UUID.randomUUID().toString())
                    .topic(emqxWebhookForm.getTopic())
                    .dataType(1)
                    .build();
            return deviceEventsService.add(deviceEvents).then(Mono.just(ResultVO.success()));
        }).defaultIfEmpty(ResultVO.error(ResponseEnum.ERROR));
    }

}

```

## Webhook接口权限

### 原因

webhook接口如果不做权限限制任何人都可以访问，可能会导致数据恶意添加。

### 配置

对于分布式的EMQ X 每台可以配置不同的账户和密码

```
emqx:
  webhook:
    users:
      makerknz: 1234567
      makerknz_1: 12313123
```

### 读取配置

```
package cn.makerknz.product.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@EnableConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "emqx.webhook")
@Data
public class EmqxConfig {

    private Map<String,String> users;

}

```

### 增加切面注解

```
package cn.makerknz.product.server.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface CheckEmqxWebhookIdentity {
}
```

### Webhook接口认证实现

```
package cn.makerknz.product.server.auth;

import cn.makerknz.product.server.config.EmqxConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: maker_knz
 * @Date: 2021/6/1/001 10:05
 * @Version 1.0
 */

@Aspect
@Component
public class EmqxWebhookIdentityAspect {

    @Autowired
    private EmqxConfig emqxConfig;

    /**
     * 切面验证webhook接口是否可以访问
     * @param point
     * @return
     */
    @Around("@annotation(cn.makerknz.product.server.annotation.CheckEmqxWebhookIdentity)")
    public Object checkLogin(ProceedingJoinPoint point) {

        try {
            HttpServletRequest request = this.getHttpServletRequest();

            // 1.从header中获取username和passowrd
            String username = request.getHeader("webhook-username");
            String password = request.getHeader("webhook-password");

            // 2.验证账户的有效性
            String userPassword = emqxConfig.getUsers().get(username);
            if (!userPassword.equals(password)) {
                throw new SecurityException("没有权限使用WebHook");
            }

            return point.proceed();
        } catch (Throwable throwable) {
            throw new SecurityException("WebHook配置错误");
        }
    }

    /**
     * 从请求头中获取HttpServletRequest
     * @return
     */
    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        return servletRequestAttributes.getRequest();
    }

}
```

### Webhook修改配置

```shell
docker ps
docker exec -it c46cb71330f1 bash
cd /opt/emqx/etc/plugins
vi vi emqx_web_hook.conf
exit
docker restart c46cb71330f1
```

修改配置文件为

```properties
##====================================================================
## WebHook
##====================================================================

## Webhook URL
##
## Value: String
web.hook.url = http://192.168.31.97:9200/emqx/webhook

## HTTP Headers
##
## Example:
## 1. web.hook.headers.content-type = application/json
## 2. web.hook.headers.accept =
##
## Value: String
web.hook.headers.content-type = application/json

## 配置接口访问权限，不使用CA证书
web.hook.headers.webhook-username = makerknz
web.hook.headers.webhook-password = 123456

## The encoding format of the payload field in the HTTP body
## The payload field only appears in the on_message_publish and on_message_delivered actions
##
## Value: plain | base64 | base62
web.hook.body.encoding_of_payload_field = plain

##-----------------------------使用https请求---------------------------------------
## PEM format file of CA's
##
## Value: File
## web.hook.ssl.cacertfile  = <PEM format file of CA's>

## Certificate file to use, PEM format assumed
##
## Value: File
## web.hook.ssl.certfile = <Certificate file to use>

## Private key file to use, PEM format assumed
##
## Value: File
## web.hook.ssl.keyfile = <Private key file to use>

## Turn on peer certificate verification
##
## Value: true | false
## web.hook.ssl.verify = false

## If not specified, the server's names returned in server's certificate is validated against
## what's provided `web.hook.url` config's host part.
## Setting to 'disable' will make EMQ X ignore unmatched server names.
## If set with a host name, the server's names returned in server's certificate is validated
## against this value.
##
## Value: String | disable
## web.hook.ssl.server_name_indication = disable

## Connection process pool size
##
## Value: Number
## HTTP 连接进程池大小。
web.hook.pool_size = 32

## Whether to enable HTTP Pipelining
##
## See: https://en.wikipedia.org/wiki/HTTP_pipelining
web.hook.enable_pipelining = true

##--------------------------------------------------------------------
## Hook Rules
## These configuration items represent a list of events should be forwarded
##
## Format:
##   web.hook.rule.<HookName>.<No> = <Spec>
## 有客户端连接时触发
web.hook.rule.client.connect.1       = {"action": "on_client_connect"}
## EMQ X下发连接应答
web.hook.rule.client.connack.1       = {"action": "on_client_connack"}
## 客户端成功接入
web.hook.rule.client.connected.1     = {"action": "on_client_connected"}
## 客户端断开连接
web.hook.rule.client.disconnected.1  = {"action": "on_client_disconnected"}
## 客户端订阅事件
web.hook.rule.client.subscribe.1     = {"action": "on_client_subscribe"}
## 客户端取消订阅事件
web.hook.rule.client.unsubscribe.1   = {"action": "on_client_unsubscribe"}
## EMQ X确认订阅事件
web.hook.rule.session.subscribed.1   = {"action": "on_session_subscribed"}
## EMQ X确认取消订阅事件
web.hook.rule.session.unsubscribed.1 = {"action": "on_session_unsubscribed"}
## 会话终止
web.hook.rule.session.terminated.1   = {"action": "on_session_terminated"}
## 发布消息
web.hook.rule.message.publish.1      = {"action": "on_message_publish"}
## 消息投递成功
web.hook.rule.message.delivered.1    = {"action": "on_message_delivered"}
## 消息已应答
web.hook.rule.message.acked.1        = {"action": "on_message_acked"}

```

## 测试

### 接口测试

### 权限测试

## 总结

这里使用WebFlux可以很好的起到消息中间件的作用，这也是为什么不使用kafka作为消息件的原因。如果使用消息订阅的方式，后面对MQTT消息处理是一件复杂的过程。

