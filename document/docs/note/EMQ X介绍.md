# EMQ X介绍及安装
[TOC]

## 为什么选择EMQ

* 它是一个开源系统，可以自己开发插件/模块，对于EMQ Broker不支持Kafaka可以编译插件。

* 稳定承载大规模的 MQTT 客户端连接，单服务器节点支持50万到100万连接。
* 支持多种协议
* 支持docker部署

## [EMQ](https://www.emqx.com/zh)

最近（2021/07/28）打开EMQ的官网发现官网已经改版，主页提出了Unified IoT Data Fabric架构范式，用来实现云边端统一实时数据[连接](https://www.emqx.com/zh/solutions/by-technologies/connect)、[移动](https://www.emqx.com/zh/solutions/by-technologies/move)、[处理](https://www.emqx.com/zh/solutions/by-technologies/process)与[分析](https://www.emqx.com/zh/solutions/by-technologies/analyze)。
我个人理解Unified IoT Data Fabric是以EMQX为核心的的一整套设备数据采集、交互、处理和分析的解决方案。

![此图只针对于EMQX企业版本](https://static.emqx.com/_nuxt/img/banner-bg.b602796.png)

此图只针对于EMQX企业版本，企业版本有更消息桥接模块。

### 连接

EMQ X是一个MQTT Broker，只要产品能实现MQTT、CoAP、LwM2M等协议的设备都可以连接EMQ X,包括智能家居产品、车连网产品、网关、边缘设计、手机端等产品的连接。
### 移动
传感数据从设备汇聚到边缘、从边缘上传到云端，亦或是控制命令从云端下发到设备，EMQ 基于标准 MQTT 消息。
### 处理
通过基于 SQL 语句的规则引擎和流式处理，无需编写代码即可实现一站式的 IoT 数据提取、过滤、转换、存储与处理，灵活集成物联网数据到 Kafka、SQL、NoSQL 与时序数据库，实现快速的应用集成与业务创新
### 分析
EMQ 为您提供基于物化视图的实时分析解决方案，支持在持续产生的物联网数据流上进行复杂的查询和分析操作，您可以通过简单的 SQL 语句查询物化视图获得实时的数据洞察，即时感知数据变化，发挥数据价值，即刻做出业务决策。
## [EMQ X 消息服务器简介](https://docs.emqx.cn/enterprise/v4.3/#emq-x-%E6%B6%88%E6%81%AF%E6%9C%8D%E5%8A%A1%E5%99%A8%E7%AE%80%E4%BB%8B)
EMQ X是云原生分布式物联网接入平台。
EMQ X (Erlang/Enterprise/Elastic MQTT Broker) 是基于 Erlang/OTP 平台开发的开源物联网 MQTT 消息服务器。
Erlang/OTP是出色的软实时 (Soft-Realtime)、低延时 (Low-Latency)、分布式 (Distributed)的语言平台。
MQTT 是轻量的 (Lightweight)、发布订阅模式 (PubSub) 的物联网消息协议。
EMQ X 设计目标是实现高可靠，并支持承载海量物联网终端的MQTT连接，支持在海量物联网设备间低延时消息路由:
* 稳定承载大规模的 MQTT 客户端连接，单服务器节点支持50万到100万连接。
* 分布式节点集群，快速低延时的消息路由，单集群支持1000万规模的路由。
* 消息服务器内扩展，支持定制多种认证方式、高效存储消息到后端数据库。
* 完整物联网协议支持，MQTT、MQTT-SN、CoAP、LwM2M、WebSocket 或私有协议支持
### emqx borker开源版和emqx enterpris企业的版的区别
emqx borker是开源的且是完全免费的，emqx enterpris则是收费的；
性能差异：十万级和百万级的差异。
功能差异：消息存储、增强规则引擎、增强数据桥接；

emqx borker基本上可以满足中小型企业的需求。
emqx borker 只能webhook、mqtt桥接。

## 单机安装
使用开源版本安装。
https://www.emqx.com/zh/downloads?product=enterprise
所有版本的安装，[参考](https://docs.emqx.cn/broker/v4.3/getting-started/install.html#%E4%BA%8C%E8%BF%9B%E5%88%B6%E5%8C%85%E5%AE%89%E8%A3%85-linux)
### Linux 安装
docker 安装
```
# 获取镜像
$ docker pull emqx/emqx:4.3.6
# 启动 Docker 容器
$ docker run -d --restart=always --name emqx -p 1883:1883 -p 8081:8081 -p 8083:8083 -p 8084:8084 -p 8883:8883 -p 18083:18083 emqx/emqx:4.3.6
```


docker-compose

```
version: '3'

services:
  emqx:
    image: emqx/emqx:4.3.6
    container_name: emqx
    ports:
      - "1883:1883" # MQTT/TCP 监听器
      - "8081:8081" # 管理 API 端口
      - "8083:8083" # MQTT/WebSocket 监听器
      - "8883:8883" # MQTT/SSL 监听器
      - "8084:8084" # MQTT/WebSocket with SSL 监听器
      - "18083:18083" # Dashboard 端口
```



> 关闭防火墙或打开1883 8081 8083 8084 8883 及 18083端口


### Windows 安装

下载 emqx-windows-4.3.6.zip ，解压
命令行下进入解压路径，启动 emqx

```
./bin/emqx start
```
> 设备如果无法连接可以关闭防火墙或打开端口
## 集群安装

## MQTT X安装

```
使用它的原因是因为界面好看
```

MQTT X[下载](https://github.com/emqx/MQTTX/releases/tag/v1.6.0)
下载MQTTX.Setup.1.6.0.exe，因为是免安装的程序可以直接将可执行文件放到安装的目录下

## 测试

### 测试emqx Borker是否安装成功

在浏览器中打开

http://{IP}:18083/

```
{IP}为安装主机的IP
默认的登录名是 admin 和密码是 public
```

设置成中文

![image-20211002122115460](http://note.makerknz.cn/image-20211002122115460.png)

### MQTT客户端连接测试

可以自己选择Mqtt客户端，我选用的是MQTT.fx

初始默认是无密码

![image-20211002132830724](http://note.makerknz.cn/image-20211002132830724.png)

配置好以上内容连接即可。

