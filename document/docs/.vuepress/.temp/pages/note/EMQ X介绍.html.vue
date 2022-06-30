<template><h1 id="emq-x介绍及安装" tabindex="-1"><a class="header-anchor" href="#emq-x介绍及安装" aria-hidden="true">#</a> EMQ X介绍及安装</h1>
<p>[TOC]</p>
<h2 id="为什么选择emq" tabindex="-1"><a class="header-anchor" href="#为什么选择emq" aria-hidden="true">#</a> 为什么选择EMQ</h2>
<ul>
<li>
<p>它是一个开源系统，可以自己开发插件/模块，对于EMQ Broker不支持Kafaka可以编译插件。</p>
</li>
<li>
<p>稳定承载大规模的 MQTT 客户端连接，单服务器节点支持50万到100万连接。</p>
</li>
<li>
<p>支持多种协议</p>
</li>
<li>
<p>支持docker部署</p>
</li>
</ul>
<h2 id="emq" tabindex="-1"><a class="header-anchor" href="#emq" aria-hidden="true">#</a> <a href="https://www.emqx.com/zh" target="_blank" rel="noopener noreferrer">EMQ<ExternalLinkIcon/></a></h2>
<p>最近（2021/07/28）打开EMQ的官网发现官网已经改版，主页提出了Unified IoT Data Fabric架构范式，用来实现云边端统一实时数据<a href="https://www.emqx.com/zh/solutions/by-technologies/connect" target="_blank" rel="noopener noreferrer">连接<ExternalLinkIcon/></a>、<a href="https://www.emqx.com/zh/solutions/by-technologies/move" target="_blank" rel="noopener noreferrer">移动<ExternalLinkIcon/></a>、<a href="https://www.emqx.com/zh/solutions/by-technologies/process" target="_blank" rel="noopener noreferrer">处理<ExternalLinkIcon/></a>与<a href="https://www.emqx.com/zh/solutions/by-technologies/analyze" target="_blank" rel="noopener noreferrer">分析<ExternalLinkIcon/></a>。
我个人理解Unified IoT Data Fabric是以EMQX为核心的的一整套设备数据采集、交互、处理和分析的解决方案。</p>
<p><img src="https://static.emqx.com/_nuxt/img/banner-bg.b602796.png" alt="此图只针对于EMQX企业版本"></p>
<p>此图只针对于EMQX企业版本，企业版本有更消息桥接模块。</p>
<h3 id="连接" tabindex="-1"><a class="header-anchor" href="#连接" aria-hidden="true">#</a> 连接</h3>
<p>EMQ X是一个MQTT Broker，只要产品能实现MQTT、CoAP、LwM2M等协议的设备都可以连接EMQ X,包括智能家居产品、车连网产品、网关、边缘设计、手机端等产品的连接。</p>
<h3 id="移动" tabindex="-1"><a class="header-anchor" href="#移动" aria-hidden="true">#</a> 移动</h3>
<p>传感数据从设备汇聚到边缘、从边缘上传到云端，亦或是控制命令从云端下发到设备，EMQ 基于标准 MQTT 消息。</p>
<h3 id="处理" tabindex="-1"><a class="header-anchor" href="#处理" aria-hidden="true">#</a> 处理</h3>
<p>通过基于 SQL 语句的规则引擎和流式处理，无需编写代码即可实现一站式的 IoT 数据提取、过滤、转换、存储与处理，灵活集成物联网数据到 Kafka、SQL、NoSQL 与时序数据库，实现快速的应用集成与业务创新</p>
<h3 id="分析" tabindex="-1"><a class="header-anchor" href="#分析" aria-hidden="true">#</a> 分析</h3>
<p>EMQ 为您提供基于物化视图的实时分析解决方案，支持在持续产生的物联网数据流上进行复杂的查询和分析操作，您可以通过简单的 SQL 语句查询物化视图获得实时的数据洞察，即时感知数据变化，发挥数据价值，即刻做出业务决策。</p>
<h2 id="emq-x-消息服务器简介" tabindex="-1"><a class="header-anchor" href="#emq-x-消息服务器简介" aria-hidden="true">#</a> <a href="https://docs.emqx.cn/enterprise/v4.3/#emq-x-%E6%B6%88%E6%81%AF%E6%9C%8D%E5%8A%A1%E5%99%A8%E7%AE%80%E4%BB%8B" target="_blank" rel="noopener noreferrer">EMQ X 消息服务器简介<ExternalLinkIcon/></a></h2>
<p>EMQ X是云原生分布式物联网接入平台。
EMQ X (Erlang/Enterprise/Elastic MQTT Broker) 是基于 Erlang/OTP 平台开发的开源物联网 MQTT 消息服务器。
Erlang/OTP是出色的软实时 (Soft-Realtime)、低延时 (Low-Latency)、分布式 (Distributed)的语言平台。
MQTT 是轻量的 (Lightweight)、发布订阅模式 (PubSub) 的物联网消息协议。
EMQ X 设计目标是实现高可靠，并支持承载海量物联网终端的MQTT连接，支持在海量物联网设备间低延时消息路由:</p>
<ul>
<li>稳定承载大规模的 MQTT 客户端连接，单服务器节点支持50万到100万连接。</li>
<li>分布式节点集群，快速低延时的消息路由，单集群支持1000万规模的路由。</li>
<li>消息服务器内扩展，支持定制多种认证方式、高效存储消息到后端数据库。</li>
<li>完整物联网协议支持，MQTT、MQTT-SN、CoAP、LwM2M、WebSocket 或私有协议支持</li>
</ul>
<h3 id="emqx-borker开源版和emqx-enterpris企业的版的区别" tabindex="-1"><a class="header-anchor" href="#emqx-borker开源版和emqx-enterpris企业的版的区别" aria-hidden="true">#</a> emqx borker开源版和emqx enterpris企业的版的区别</h3>
<p>emqx borker是开源的且是完全免费的，emqx enterpris则是收费的；
性能差异：十万级和百万级的差异。
功能差异：消息存储、增强规则引擎、增强数据桥接；</p>
<p>emqx borker基本上可以满足中小型企业的需求。
emqx borker 只能webhook、mqtt桥接。</p>
<h2 id="单机安装" tabindex="-1"><a class="header-anchor" href="#单机安装" aria-hidden="true">#</a> 单机安装</h2>
<p>使用开源版本安装。
https://www.emqx.com/zh/downloads?product=enterprise
所有版本的安装，<a href="https://docs.emqx.cn/broker/v4.3/getting-started/install.html#%E4%BA%8C%E8%BF%9B%E5%88%B6%E5%8C%85%E5%AE%89%E8%A3%85-linux" target="_blank" rel="noopener noreferrer">参考<ExternalLinkIcon/></a></p>
<h3 id="linux-安装" tabindex="-1"><a class="header-anchor" href="#linux-安装" aria-hidden="true">#</a> Linux 安装</h3>
<p>docker 安装</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code># 获取镜像
$ docker pull emqx/emqx:4.3.6
# 启动 Docker 容器
$ docker run -d --restart=always --name emqx -p 1883:1883 -p 8081:8081 -p 8083:8083 -p 8084:8084 -p 8883:8883 -p 18083:18083 emqx/emqx:4.3.6
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br></div></div><p>docker-compose</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>version: '3'

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
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br><span class="line-number">7</span><br><span class="line-number">8</span><br><span class="line-number">9</span><br><span class="line-number">10</span><br><span class="line-number">11</span><br><span class="line-number">12</span><br><span class="line-number">13</span><br></div></div><blockquote>
<p>关闭防火墙或打开1883 8081 8083 8084 8883 及 18083端口</p>
</blockquote>
<h3 id="windows-安装" tabindex="-1"><a class="header-anchor" href="#windows-安装" aria-hidden="true">#</a> Windows 安装</h3>
<p>下载 emqx-windows-4.3.6.zip ，解压
命令行下进入解压路径，启动 emqx</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>./bin/emqx start
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br></div></div><blockquote>
<p>设备如果无法连接可以关闭防火墙或打开端口</p>
</blockquote>
<h2 id="集群安装" tabindex="-1"><a class="header-anchor" href="#集群安装" aria-hidden="true">#</a> 集群安装</h2>
<h2 id="mqtt-x安装" tabindex="-1"><a class="header-anchor" href="#mqtt-x安装" aria-hidden="true">#</a> MQTT X安装</h2>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>使用它的原因是因为界面好看
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br></div></div><p>MQTT X<a href="https://github.com/emqx/MQTTX/releases/tag/v1.6.0" target="_blank" rel="noopener noreferrer">下载<ExternalLinkIcon/></a>
下载MQTTX.Setup.1.6.0.exe，因为是免安装的程序可以直接将可执行文件放到安装的目录下</p>
<h2 id="测试" tabindex="-1"><a class="header-anchor" href="#测试" aria-hidden="true">#</a> 测试</h2>
<h3 id="测试emqx-borker是否安装成功" tabindex="-1"><a class="header-anchor" href="#测试emqx-borker是否安装成功" aria-hidden="true">#</a> 测试emqx Borker是否安装成功</h3>
<p>在浏览器中打开</p>
<p>http://{IP}:18083/</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>{IP}为安装主机的IP
默认的登录名是 admin 和密码是 public
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br></div></div><p>设置成中文</p>
<p><img src="http://note.makerknz.cn/image-20211002122115460.png" alt="image-20211002122115460"></p>
<h3 id="mqtt客户端连接测试" tabindex="-1"><a class="header-anchor" href="#mqtt客户端连接测试" aria-hidden="true">#</a> MQTT客户端连接测试</h3>
<p>可以自己选择Mqtt客户端，我选用的是MQTT.fx</p>
<p>初始默认是无密码</p>
<p><img src="http://note.makerknz.cn/image-20211002132830724.png" alt="image-20211002132830724"></p>
<p>配置好以上内容连接即可。</p>
</template>
